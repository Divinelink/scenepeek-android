package com.divinelink.core.domain.details.person

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.person.GroupedPersonCredits
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.KnownForDepartment
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.model.person.credits.PersonCredit
import com.divinelink.core.network.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

data class PersonDetailsParams(
  val id: Long,
  val knownForDepartment: String?,
)

class FetchPersonDetailsUseCase(
  private val repository: PersonRepository,
  val dispatcher: DispatcherProvider,
) : FlowUseCase<PersonDetailsParams, PersonDetailsResult>(dispatcher.default) {

  override fun execute(parameters: PersonDetailsParams): Flow<Result<PersonDetailsResult>> =
    channelFlow {
      if (parameters.knownForDepartment != null) {
        launch {
          repository.fetchPersonDetails(parameters.id)
            .collect { result ->
              result.fold(
                onFailure = {
                  Timber.e(it)
                  send(Result.failure(PersonDetailsResult.DetailsFailure))
                },
                onSuccess = {
                  send(Result.success(PersonDetailsResult.DetailsSuccess(result.data)))
                },
              )
            }
        }
      }

      val asyncDetails = if (parameters.knownForDepartment == null) {
        async {
          repository.fetchPersonDetails(parameters.id)
            .map { result ->
              result.fold(
                onFailure = {
                  Timber.e(it)
                  Result.failure(PersonDetailsResult.DetailsFailure)
                },
                onSuccess = { Result.success(PersonDetailsResult.DetailsSuccess(it)) },
              )
            }
            .first()
        }
      } else {
        null
      }

      asyncDetails?.await()?.let { send(it) }

      launch {
        val knownForDepartment = parameters.knownForDepartment
          ?: asyncDetails?.await()?.data?.personDetails?.person?.knownForDepartment
          ?: KnownForDepartment.Acting.value

        repository.fetchPersonCredits(parameters.id)
          .catch { Timber.e(it) }
          .collect { result ->
            when (result) {
              is Resource.Error -> Timber.d(result.error)
              is Resource.Loading<PersonCombinedCredits?> -> result.data?.let { data ->
                calculateCredits(knownForDepartment, data)
              }
              is Resource.Success<PersonCombinedCredits?> -> result.data?.let { data ->
                calculateCredits(knownForDepartment, data)
              }
            }
          }
      }
    }

  private suspend fun ProducerScope<Result<PersonDetailsResult>>.calculateCredits(
    knownForDepartment: String,
    data: PersonCombinedCredits,
  ) {
    val knownForCredits = calculateKnownForCredits(
      department = knownForDepartment,
      result = Result.success(data),
    )

    val credits = findCreditsForPerson(
      department = knownForDepartment,
      result = Result.success(data),
    )

    val movies = credits.mapValues { department ->
      department.value.filter { it.media is MediaItem.Media.Movie }
    }.filter { it.value.isNotEmpty() }

    val tvShows = credits.mapValues { department ->
      department.value.filter { it.media is MediaItem.Media.TV }
    }.filter { it.value.isNotEmpty() }

    send(
      Result.success(
        PersonDetailsResult.CreditsSuccess(
          knownForCredits = knownForCredits,
          knownForDepartment = knownForDepartment,
          movies = movies,
          tvShows = tvShows,
        ),
      ),
    )
  }

  private fun calculateMovieScore(credit: PersonCredit): Double {
    val order = (credit.role as? PersonRole.MovieActor)?.order ?: Int.MAX_VALUE
    return (credit.media.voteCount / (order + 1)) * (0.04 * credit.media.popularity)
  }

  private fun calculateScore(media: PersonCredit) = when (media.role) {
    is PersonRole.MovieActor -> calculateMovieScore(media)
    is PersonRole.SeriesActor -> calculateTvScore(media)
    else -> 1.0
  }

  private fun calculateTvScore(credit: PersonCredit): Double {
    val episodeCount = (credit.role as? PersonRole.SeriesActor)?.totalEpisodes ?: 0
    return credit.media.voteCount * episodeCount / (0.1 * credit.media.popularity)
  }

  private fun calculateKnownForCredits(
    department: String,
    result: Result<PersonCombinedCredits>,
  ): List<PersonCredit> = if (department == KnownForDepartment.Acting.value) {
    result.data.cast
      .sortedByDescending { calculateScore(it) }
      .distinctBy { it.media.id }
      .take(10)
      .sortedByDescending { it.media.voteAverage }
  } else {
    result.data.crew
      .filter { (it.role as? PersonRole.Crew)?.department == department }
      .sortedByDescending { it.media.popularity }
      .distinctBy { it.media.id }
      .take(10)
  }

  private fun findCreditsForPerson(
    department: String,
    result: Result<PersonCombinedCredits>,
  ): GroupedPersonCredits {
    val map = mutableMapOf<String, List<PersonCredit>>()

    val allCrewDepartments = result
      .data
      .crew
      .mapNotNull { (it.role as? PersonRole.Crew)?.department }
      .distinct()

    val allDepartments = (allCrewDepartments + KnownForDepartment.Acting.value).distinct()
    val departmentsToProcess = listOf(department) + (allDepartments - department)

    departmentsToProcess.forEach { dep ->
      val credits = if (dep == KnownForDepartment.Acting.value) {
        result.data.cast
          .distinctBy { it.media.id }
      } else {
        result.data.crew
          .filter { (it.role as? PersonRole.Crew)?.department == dep }
          .distinctBy { it.media.id }
      }
      map[dep] = credits
    }

    return map
  }
}
