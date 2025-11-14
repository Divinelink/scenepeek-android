package com.divinelink.scenepeek.fakes.usecase

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.scenepeek.home.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetPopularMoviesUseCase {

  val mock: GetPopularMoviesUseCase = mock()

  fun mockFetchPopularMovies(response: Flow<MediaListResult>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }

  fun mockFetchPopularMovies(response: MediaListResult) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
