package com.divinelink.scenepeek.fakes.usecase

import com.divinelink.core.domain.search.FetchMultiInfoSearchUseCase
import com.divinelink.core.domain.search.MultiSearchResult
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeFetchMultiInfoSearchUseCase {

  val mock: FetchMultiInfoSearchUseCase = mock()

  fun mockFetchMultiInfoSearch(response: Result<MultiSearchResult>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
