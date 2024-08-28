package com.divinelink.core.data.person.repository

import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.credits.PersonCombinedCredits
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

  fun fetchPersonDetails(id: Long): Flow<Result<PersonDetails>>

  fun fetchPersonCredits(id: Long): Flow<Result<PersonCombinedCredits>>

  fun fetchPersonChanges(id: Long): Flow<Result<List<PersonDetails>>>
}
