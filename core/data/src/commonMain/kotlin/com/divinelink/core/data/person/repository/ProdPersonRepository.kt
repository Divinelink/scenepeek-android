package com.divinelink.core.data.person.repository

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.data.person.credits.mapper.map
import com.divinelink.core.data.person.credits.mapper.toEntityCast
import com.divinelink.core.data.person.credits.mapper.toEntityCrew
import com.divinelink.core.data.person.credits.mapper.toMediaEntities
import com.divinelink.core.data.person.details.mapper.map
import com.divinelink.core.data.person.details.mapper.mapToEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.person.PersonChangeField
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.change.Changes
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import com.divinelink.core.network.Resource
import com.divinelink.core.network.changes.mapper.map
import com.divinelink.core.network.details.person.service.PersonService
import com.divinelink.core.network.media.model.changes.ChangesParameters
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class ProdPersonRepository(
  private val service: PersonService,
  private val dao: PersonDao,
  private val mediaDao: MediaDao,
  private val clock: Clock,
  val dispatcher: DispatcherProvider,
) : PersonRepository {

  override fun fetchPersonDetails(id: Long): Flow<Result<PersonDetails>> = channelFlow {
    dao.fetchPersonById(id).collect { personDetails ->
      if (personDetails != null) {
        send(Result.success(personDetails.map()))
        return@collect
      } else {
        service.fetchPersonDetails(id).collectLatest { response ->
          dao.insertPerson(response.mapToEntity(clock.currentEpochSeconds()))
        }
      }
    }
  }

  override fun fetchPersonCredits(id: Long): Flow<Resource<PersonCombinedCredits?>> =
    networkBoundResource(
      query = {
        combine(
          mediaDao.getFavoriteMediaIds(MediaType.TV),
          mediaDao.getFavoriteMediaIds(MediaType.MOVIE),
          dao.fetchPersonCombinedCredits(id),
        ) { tvIds, movieIds, credits ->

          credits?.map(
            favoriteMovieIds = movieIds,
            favoriteTvIds = tvIds,
          )
        }
      },
      fetch = { service.fetchPersonCombinedCredits(id) },
      saveFetchResult = { response ->
        response.collect { credits ->
          dao.insertPersonCredits(credits.id)
          dao.insertPersonCrewCredits(credits.toEntityCrew())
          dao.insertPersonCastCredits(credits.toEntityCast())

          val media = credits.toMediaEntities()
          mediaDao.insertMediaEntities(media)
        }
      },
      shouldFetch = { data -> data == null },
    )

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
    profilePath = profilePath,
    insertedAt = insertedAt,
  )

  override fun deleteFromPerson(
    id: Long,
    field: PersonChangeField,
  ) = dao.deleteFromPerson(id, field)
}
