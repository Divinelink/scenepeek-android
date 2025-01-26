package com.divinelink.core.domain.details.person

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.FlowUseCase
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.person.details.model.PersonDetailsResult
import com.divinelink.core.data.person.repository.PersonRepository
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.person.KnownForDepartment
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.model.person.credits.PersonCredit
import kotlinx.coroutines.async
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
) : FlowUseCase<PersonDetailsParams, PersonDetailsResult>(dispatcher.io) {

  override fun execute(parameters: PersonDetailsParams): Flow<Result<PersonDetailsResult>> =
    channelFlow {
      if (parameters.knownForDepartment != null) {
        launch(dispatcher.io) {
          repository.fetchPersonDetails(parameters.id)
            .catch {
              Timber.e(it)
              send(Result.failure(PersonDetailsResult.DetailsFailure))
            }
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
        async(dispatcher.io) {
          repository.fetchPersonDetails(parameters.id)
            .catch {
              Timber.e(it)
              emit(Result.failure(PersonDetailsResult.DetailsFailure))
            }
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

      launch(dispatcher.io) {
        val knownForDepartment = parameters.knownForDepartment
          ?: asyncDetails?.await()?.data?.personDetails?.person?.knownForDepartment
          ?: KnownForDepartment.Acting.value

        repository.fetchPersonCredits(parameters.id)
          .catch { Timber.e(it) }
          .collect { result ->
            result.fold(
              onFailure = { Timber.e(it) },
              onSuccess = {
                val knownForCredits = calculateKnownForCredits(
                  department = knownForDepartment,
                  result = result,
                )

                send(
                  Result.success(
                    PersonDetailsResult.CreditsSuccess(
                      movies = result.data.cast.filter { it.mediaItem is MediaItem.Media.Movie },
                      tvShows = result.data.cast.filter { it.mediaItem is MediaItem.Media.TV },
                      knownForCredits = knownForCredits,
                    ),
                  ),
                )
              },
            )
          }
      }
    }

  private fun calculateMovieScore(media: PersonCredit): Double {
    val order = (media.role as? PersonRole.MovieActor)?.order ?: Int.MAX_VALUE
    return (media.voteCount / (order + 1)) * (0.04 * media.popularity)
  }

  private fun calculateScore(media: PersonCredit) = when (media.role) {
    is PersonRole.MovieActor -> calculateMovieScore(media)
    is PersonRole.SeriesActor -> calculateTvScore(media)
    else -> 1.0
  }

  private fun calculateTvScore(media: PersonCredit): Double {
    val episodeCount = (media.role as? PersonRole.SeriesActor)?.totalEpisodes ?: 0
    return media.voteCount * episodeCount / (0.1 * media.popularity)
  }

  private fun calculateKnownForCredits(
    department: String,
    result: Result<PersonCombinedCredits>,
  ): List<PersonCredit> = if (department == KnownForDepartment.Acting.value) {
    result.data.cast
      .sortedByDescending { calculateScore(it) }
      .distinctBy { it.id }
      .take(10)
      .sortedByDescending { it.voteAverage }
  } else {
    result.data.crew
      .filter { (it.role as? PersonRole.Crew)?.department == department }
      .sortedByDescending { it.popularity }
      .distinctBy { it.id }
      .take(10)
  }
}
