package com.divinelink.core.database.credits.dao

import com.divinelink.core.database.cast.PersonEntity
import com.divinelink.core.database.cast.PersonRoleEntity
import com.divinelink.core.database.credits.ShowCastRoleEntity
import com.divinelink.core.database.credits.crew.SeriesCrew
import com.divinelink.core.database.credits.crew.SeriesCrewJob
import com.divinelink.core.database.credits.model.AggregateCreditsEntity
import com.divinelink.core.model.details.Person
import kotlinx.coroutines.flow.Flow

interface CreditsDao {

  fun checkIfAggregateCreditsExist(id: Long): Flow<Boolean>
  fun fetchAllCredits(id: Long): Flow<AggregateCreditsEntity>

  fun insertAggregateCredits(aggregateCreditsId: Long)

  fun insertPersons(persons: List<PersonEntity>)
  fun insertRoles(roles: List<Pair<PersonRoleEntity, ShowCastRoleEntity>>)

  fun fetchAllCastWithRoles(id: Long): Flow<List<Person>>

  fun insertCrew(crew: List<SeriesCrew>)
  fun insertCrewJobs(jobs: List<SeriesCrewJob>)

  fun fetchAllCrewJobs(aggregateCreditId: Long): Flow<List<Person>>
}
