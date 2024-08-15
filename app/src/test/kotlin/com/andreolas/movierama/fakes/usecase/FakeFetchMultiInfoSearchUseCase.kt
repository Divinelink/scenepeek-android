package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.FetchMultiInfoSearchUseCase
import com.andreolas.movierama.home.domain.usecase.MultiSearchResult
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
