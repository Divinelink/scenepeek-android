package com.divinelink.core.database.credits.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.cacheExpiresAtToEpochSeconds
import com.divinelink.core.database.credits.AggregateCredits
import com.divinelink.core.database.credits.cast.SeriesCast
import com.divinelink.core.database.credits.cast.SeriesCastRole
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.database.credits.mapper.toEntity
import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.database.credits.model.CastEntity
import com.divinelink.core.database.credits.model.CrewEntity
import com.divinelink.core.database.currentEpochSeconds
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

  override fun insertCast(cast: List<SeriesCast>) = database.transaction {
    cast.forEach { castMember ->
      database.seriesCastQueries.insertSeriesCast(castMember)
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

  override fun insertCastRoles(roles: List<SeriesCastRole>) = database.transaction {
    roles.forEach { role ->
      database.seriesCastRoleQueries.insertRole(seriesCastRole = role)
    }
  }

  override fun fetchAllCastWithRoles(id: Long): Flow<List<CastEntity>> = database
    .transactionWithResult {
      val rolesByCastId = database.seriesCastRoleQueries
        .fetchRoles(aggregateCreditId = id)
        .executeAsList()
        .groupBy { it.castId }

      database
        .seriesCastQueries
        .fetchSeriesCast(id)
        .asFlow()
        .mapToList(dispatcher.io)
        .map { listOfCast ->
          listOfCast
            .map { cast ->
              val roles = rolesByCastId[cast.id] ?: emptyList()
              cast.toEntity(roles = roles)
            }
            .filter { it.roles.isNotEmpty() }
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

  override fun fetchAllCrewJobs(aggregateCreditId: Long): Flow<List<CrewEntity>> =
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
              crew.toEntity(roles = jobs)
            }
            .filter { it.roles.isNotEmpty() }
        }
    }
}
