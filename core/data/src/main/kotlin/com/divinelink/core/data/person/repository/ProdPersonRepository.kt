package com.divinelink.core.data.person.repository

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.data.person.credits.mapper.map
import com.divinelink.core.data.person.credits.mapper.toEntityCast
import com.divinelink.core.data.person.credits.mapper.toEntityCrew
import com.divinelink.core.data.person.details.mapper.map
import com.divinelink.core.data.person.details.mapper.mapToEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.change.Changes
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.network.changes.mapper.map
import com.divinelink.core.network.details.person.service.PersonService
import com.divinelink.core.network.media.model.changes.ChangesParameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import timber.log.Timber

class ProdPersonRepository(
  private val service: PersonService,
  private val dao: PersonDao,
  private val clock: Clock,
  val dispatcher: DispatcherProvider,
) : PersonRepository {

  override fun fetchPersonDetails(id: Long): Flow<Result<PersonDetails>> = channelFlow {
    dao.fetchPersonById(id).collectLatest { personDetails ->
      if (personDetails != null) {
        Timber.d("Person details | ${personDetails.name} | found in database")
        send(Result.success(personDetails.map()))
        return@collectLatest
      } else {
        Timber.d("Person details not found in database")
        service.fetchPersonDetails(id).collectLatest { response ->
          dao.insertPerson(response.mapToEntity(clock.currentEpochSeconds()))
        }
      }
    }
  }

  override fun fetchPersonCredits(id: Long): Flow<Result<PersonCombinedCredits>> = channelFlow {
    dao.fetchPersonCombinedCredits(id).collectLatest { personCredits ->
      if (personCredits != null) {
        Timber.d("Person credits | ${personCredits.id} | found in database")
        send(Result.success(personCredits.map()))
      } else {
        Timber.d("Person credits not found in database")
        service.fetchPersonCombinedCredits(id).collectLatest { response ->
          dao.insertPersonCredits(response.id)
          dao.insertPersonCrewCredits(response.toEntityCrew())
          dao.insertPersonCastCredits(response.toEntityCast())
        }
      }
    }
  }

  override fun fetchPersonChanges(
    id: Long,
    params: ChangesParameters,
  ): Flow<Result<Changes>> = service
    .fetchPersonChanges(id, params)
    .map { response ->
      Result.success(response.map())
    }

  override fun updatePerson(
    id: Long,
    biography: String?,
    name: String?,
    birthday: String?,
    deathday: String?,
    gender: Int?,
    homepage: String?,
    imdbId: String?,
    knownForDepartment: String?,
    placeOfBirth: String?,
    popularity: Double?,
    profilePath: String?,
    insertedAt: String?,
  ) = dao.updatePerson(
    id = id,
    biography = biography,
    name = name,
    birthday = birthday,
    deathday = deathday,
    gender = gender,
    homepage = homepage,
    imdbId = imdbId,
    knownForDepartment = knownForDepartment,
    placeOfBirth = placeOfBirth,
    popularity = popularity,
    profilePath = profilePath,
    insertedAt = insertedAt,
  )
}
