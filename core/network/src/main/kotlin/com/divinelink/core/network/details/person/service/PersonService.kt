package com.divinelink.core.network.details.person.service

import com.divinelink.core.network.details.person.model.PersonDetailsApi
import kotlinx.coroutines.flow.Flow

interface PersonService {

  fun fetchPersonDetails(id: Long): Flow<PersonDetailsApi>
}
