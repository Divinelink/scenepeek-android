package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.SearchResult
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
