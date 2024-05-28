package com.andreolas.movierama.fakes.usecase.details

import com.andreolas.movierama.details.domain.usecase.AddToWatchlistUseCase
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeAddToWatchlistUseCase {

  val mock: AddToWatchlistUseCase = mock()

  fun mockAddToWatchlist(
    response: Flow<Result<Unit>>,
  ) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
