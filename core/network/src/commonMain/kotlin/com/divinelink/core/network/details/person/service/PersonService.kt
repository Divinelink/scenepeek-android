package com.divinelink.core.network.details.person.service

import com.divinelink.core.network.changes.model.api.ChangesResponseApi
import com.divinelink.core.network.details.person.model.PersonCreditsApi
import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.network.media.model.changes.ChangesParameters
import kotlinx.coroutines.flow.Flow

interface PersonService {

  fun fetchPersonDetails(id: Long): Flow<PersonDetailsApi>

  fun fetchPersonCombinedCredits(id: Long): Flow<PersonCreditsApi>

  fun fetchPersonChanges(
    id: Long,
    body: ChangesParameters,
  ): Flow<ChangesResponseApi>
}
