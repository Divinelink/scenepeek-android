package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.details.media.AddToWatchlistUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeAddToWatchlistUseCase {

  val mock: AddToWatchlistUseCase = mock()

  suspend fun mockAddToWatchlist(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
