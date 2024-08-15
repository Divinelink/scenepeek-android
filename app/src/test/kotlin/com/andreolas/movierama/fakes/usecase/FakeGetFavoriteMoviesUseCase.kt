package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import com.divinelink.core.data.media.repository.MediaListResult
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetFavoriteMoviesUseCase {

  val mock: GetFavoriteMoviesUseCase = mock()

  fun mockGetFavoriteMovies(response: MediaListResult) {
    whenever(mock.invoke(Unit)).thenReturn(flowOf(response))
//    coEvery {
//      mock.invoke(Unit)
//    } returns flowOf(response)
  }
}
