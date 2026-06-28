package com.divinelink.core.testing.service

import com.divinelink.core.network.awards.AwardsService
import com.divinelink.core.network.awards.model.awards.AwardsResponse
import com.divinelink.core.network.awards.model.category.CeremonyCategoriesResponse
import com.divinelink.core.network.awards.model.ceremony.CeremoniesResponse
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestAwardsService {
  val mock: AwardsService = mock()

  suspend fun mockFetchAwardCeremonies(response: Result<CeremoniesResponse>) {
    whenever(mock.fetchAwardCeremonies()).thenReturn(response)
  }

  suspend fun mockFetchCeremonyCategories(response: Result<CeremonyCategoriesResponse>) {
    whenever(mock.fetchCeremonyCategories(id = any())).thenReturn(response)
  }

  suspend fun mockFetchAwards(response: Result<AwardsResponse>) {
    whenever(mock.fetchAwards(ceremonyId = any(), categoryId = any())).thenReturn(response)
  }
}
