package com.divinelink.core.database.credits.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import com.divinelink.core.commons.di.IoDispatcher
import com.divinelink.core.database.Database
import com.divinelink.core.database.credits.cast.SeriesCast
import com.divinelink.core.database.credits.cast.SeriesCastRole
import com.divinelink.core.database.credits.cast.SeriesCastWithRole
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.database.credits.crew.SeriesCrewWithJob
import com.divinelink.core.database.credits.mapper.toEntity
import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.database.credits.model.CastEntity
import com.divinelink.core.database.credits.model.CrewEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdCreditsDao @Inject constructor(
  private val database: Database,
  @IoDispatcher val dispatcher: CoroutineDispatcher,
) : CreditsDao {

  override fun insertAggregateCredits(aggregateCreditsId: Long) = database
    .aggregateCreditsQueries
    .insert(id = aggregateCreditsId)

  override fun checkIfAggregateCreditsExist(id: Long): Flow<Boolean> = database
    .aggregateCreditsQueries
    .checkIfExist(id = id)
    .asFlow()
    .mapToOneOrDefault(
      defaultValue = false,
      context = dispatcher,
    )

  override fun insertCast(cast: List<SeriesCast>) = cast.forEach { castMember ->
    database.seriesCastQueries.insertSeriesCast(castMember)
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

  override fun insertCastRoles(roles: List<SeriesCastRole>) = roles.forEach { role ->
    database.seriesCastRoleQueries.insertRole(seriesCastRole = role)
  }

  override fun fetchAllCastWithRoles(id: Long): Flow<List<CastEntity>> = database
    .seriesCastQueries
    .fetchSeriesCastWithRoles(id)
    .asFlow()
    .mapToList(dispatcher)
    .map { listOfCast ->
      listOfCast.map(SeriesCastWithRole::toEntity)
    }

  override fun insertCrew(crew: List<SeriesCrew>) = crew.forEach { crewMember ->
    database.seriesCrewQueries.insertCrew(crewMember)
  }

  override fun insertCrewJobs(jobs: List<SeriesCrewJob>) = jobs.forEach { job ->
    database.seriesCrewJobQueries.insertCrewJob(seriesCrewJob = job)
  }

  override fun fetchAllCrewJobs(aggregateCreditId: Long): Flow<List<CrewEntity>> = database
    .seriesCrewQueries
    .fetchSeriesCrewWithJobs(aggregateCreditId)
    .asFlow()
    .mapToList(dispatcher)
    .map { listOfCrewWithJob ->
      listOfCrewWithJob.map(SeriesCrewWithJob::toEntity)
    }
}
