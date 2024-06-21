package com.andreolas.movierama.fakes.usecase

import com.divinelink.feature.details.ui.MovieDetailsResult
import com.divinelink.feature.details.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetMoviesDetailsUseCase {

  val mock: GetMovieDetailsUseCase = mock()

  fun mockFetchMovieDetails(response: Flow<Result<MovieDetailsResult>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
