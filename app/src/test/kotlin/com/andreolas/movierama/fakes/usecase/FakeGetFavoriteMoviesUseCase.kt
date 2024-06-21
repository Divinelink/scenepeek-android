package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import com.divinelink.core.data.media.repository.MediaListResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class FakeGetFavoriteMoviesUseCase {

  val mock: GetFavoriteMoviesUseCase = mockk()

  fun mockGetFavoriteMovies(response: MediaListResult) {
    coEvery {
      mock.invoke(Unit)
    } returns flowOf(response)
  }
}
