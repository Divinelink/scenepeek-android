package com.divinelink.core.database.credits.dao

import com.divinelink.core.database.credits.cast.SeriesCast
import com.divinelink.core.database.credits.cast.SeriesCastRole
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.database.credits.model.CastEntity
import com.divinelink.core.database.credits.model.CrewEntity
import kotlinx.coroutines.flow.Flow

interface CreditsDao {

  fun checkIfAggregateCreditsExist(id: Long): Flow<Boolean>
  fun fetchAllCredits(id: Long): Flow<AggregateCreditsEntity>

  fun insertAggregateCredits(aggregateCreditsId: Long)

  fun insertCast(cast: List<SeriesCast>)
  fun insertCastRoles(roles: List<SeriesCastRole>)

  fun fetchAllCastWithRoles(id: Long): Flow<List<CastEntity>>

  fun insertCrew(crew: List<SeriesCrew>)
  fun insertCrewJobs(jobs: List<SeriesCrewJob>)

  fun fetchAllCrewJobs(aggregateCreditId: Long): Flow<List<CrewEntity>>
}
