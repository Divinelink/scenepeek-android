package com.divinelink.core.database.credits.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.cacheExpiresAtToEpochSeconds
import com.divinelink.core.database.cast.PersonEntity
import com.divinelink.core.database.cast.PersonRoleEntity
import com.divinelink.core.database.credits.AggregateCredits
import com.divinelink.core.database.credits.ShowCastRoleEntity
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.database.credits.mapper.toPerson
import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.details.Person
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class ProdCreditsDao(
  private val database: Database,
  private val clock: Clock,
  val dispatcher: DispatcherProvider,
) : CreditsDao {

  override fun insertAggregateCredits(aggregateCreditsId: Long) {
    val expirationTimestamp = clock.cacheExpiresAtToEpochSeconds()

    database
      .aggregateCreditsQueries
      .insert(
        aggregateCredits = AggregateCredits(
          id = aggregateCreditsId,
          expiresAtEpochSeconds = expirationTimestamp,
        ),
      )
  }

  override fun checkIfAggregateCreditsExist(id: Long): Flow<Boolean> {
    val currentTime = clock.currentEpochSeconds()

    return database
      .aggregateCreditsQueries
      .checkIfExistAndNotExpired(id = id, expiresAtEpochSeconds = currentTime)
      .asFlow()
      .mapToOneOrDefault(
        defaultValue = false,
        context = dispatcher.io,
      )
  }

  override fun insertPersons(persons: List<PersonEntity>) = database.transaction {
    persons.forEach { person ->
      database.personEntityQueries.insertPerson(person)
    }
  }

  override fun insertRoles(roles: List<Pair<PersonRoleEntity, ShowCastRoleEntity>>) =
    database.transaction {
      roles.forEach { role ->
        database.personRoleEntityQueries.insertRole(role.first)
        database.showCastRoleQueries.insertShowCastRole(role.second)
      }
    }

  override fun fetchAllCredits(id: Long): Flow<AggregateCreditsEntity> {
    val cast = fetchAllCastWithRoles(id)
    val crew = fetchAllCrewJobs(id)

    return combine(cast, crew) { castList, crewList ->
      AggregateCreditsEntity(
        id = id,
        cast = castList,
        crew = crewList,
      )
    }
  }

  override fun fetchAllCastWithRoles(id: Long): Flow<List<Person>> = database
    .transactionWithResult {
      val rolesByCastId = database
        .showCastRoleQueries
        .fetchCastRoles(showId = id)
        .executeAsList()
        .groupBy { it.castId }

      database
        .personEntityQueries
        .fetchShowCast(id)
        .asFlow()
        .mapToList(dispatcher.io)
        .map { listOfCast ->
          listOfCast
            .map { cast ->
              val roles = rolesByCastId[cast.id] ?: emptyList()
              cast.toPerson(
                roles = roles.map {
                  PersonRole.SeriesActor(
                    character = it.character,
                    creditId = it.creditId,
                    totalEpisodes = it.episodeCount.toInt(),
                  )
                },
              )
            }
            .filter { it.role.isNotEmpty() }
            .distinctBy { it.id }
        }
    }

  override fun insertCrew(crew: List<SeriesCrew>) = database.transaction {
    crew.forEach { crewMember ->
      database.seriesCrewQueries.insertCrew(crewMember)
    }
  }

  override fun insertCrewJobs(jobs: List<SeriesCrewJob>) = database.transaction {
    jobs.forEach { job ->
      database.seriesCrewJobQueries.insertCrewJob(seriesCrewJob = job)
    }
  }

  override fun fetchAllCrewJobs(aggregateCreditId: Long): Flow<List<Person>> =
    database.transactionWithResult {
      val crewJobs = database.seriesCrewJobQueries
        .fetchCrewJobs(aggregateCreditId)
        .executeAsList()
        .groupBy { Pair(it.crewId, it.department) }

      database
        .seriesCrewQueries
        .fetchSeriesCrew(aggregateCreditId)
        .asFlow()
        .mapToList(dispatcher.io)
        .map { listOfCrew ->
          listOfCrew
            .map { crew ->
              val jobs = crewJobs[Pair(crew.id, crew.department)] ?: emptyList()
              crew.toPerson(roles = jobs)
            }
            .filter { it.role.isNotEmpty() }
        }
    }
}
