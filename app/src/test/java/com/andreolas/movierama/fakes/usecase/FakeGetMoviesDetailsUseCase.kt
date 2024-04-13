package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetMoviesDetailsUseCase {

  val mock: GetMovieDetailsUseCase = mock()

  fun mockFetchMovieDetails(
    response: Flow<Result<MovieDetailsResult>>,
  ) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
