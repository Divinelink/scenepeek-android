package com.divinelink.core.testing.service

import com.divinelink.core.network.changes.model.api.ChangesResponseApi
import com.divinelink.core.network.details.person.model.PersonCreditsApi
import com.divinelink.core.network.details.person.model.PersonDetailsApi
import com.divinelink.core.network.details.person.service.PersonService
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestPersonService {
  val mock: PersonService = mock()

  fun mockFetchPersonDetails(response: PersonDetailsApi) {
    whenever(mock.fetchPersonDetails(id = any())).thenReturn(flowOf(response))
  }

  fun mockFetchPersonCombinedCredits(response: PersonCreditsApi) {
    whenever(mock.fetchPersonCombinedCredits(id = any())).thenReturn(flowOf(response))
  }

  fun mockFetchPersonChanges(response: ChangesResponseApi) {
    whenever(mock.fetchPersonChanges(id = any(), body = any())).thenReturn(flowOf(response))
  }
}
