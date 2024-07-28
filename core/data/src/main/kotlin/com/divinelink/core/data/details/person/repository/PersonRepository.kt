package com.divinelink.core.data.details.person.repository

import com.divinelink.core.model.details.person.PersonDetails
import kotlinx.coroutines.flow.Flow

interface PersonRepository {

  fun fetchPersonDetails(id: Long): Flow<Result<PersonDetails>>
}
