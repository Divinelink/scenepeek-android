package com.divinelink.core.testing.service

import com.divinelink.core.network.omdb.model.OMDbResponseApi
import com.divinelink.core.network.omdb.service.OMDbService
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestOMDbService {
  val mock: OMDbService = mock()

  fun mockFetchImdbDetails(response: OMDbResponseApi) {
    whenever(mock.fetchExternalRatings(imdbId = any())).thenReturn(flowOf(response))
  }
}
