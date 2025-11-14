package com.divinelink.scenepeek.fakes.usecase

import com.divinelink.scenepeek.home.usecase.GetSearchMoviesUseCase
import com.divinelink.scenepeek.home.usecase.SearchResult
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Deprecated("Use FakeFetchMultiInfoSearch instead")
class FakeGetSearchMoviesUseCase {

  val mock: GetSearchMoviesUseCase = mock()

  fun mockFetchSearchMovies(response: Result<SearchResult>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
