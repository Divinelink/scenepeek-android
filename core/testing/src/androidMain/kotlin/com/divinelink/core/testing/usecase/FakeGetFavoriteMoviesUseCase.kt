package com.divinelink.core.testing.usecase

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.core.domain.GetFavoriteMoviesUseCase
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetFavoriteMoviesUseCase {

  val mock: GetFavoriteMoviesUseCase = mock()

  fun mockGetFavoriteMovies(response: MediaListResult) {
    whenever(mock.invoke(Unit)).thenReturn(flowOf(response))
  }
}
