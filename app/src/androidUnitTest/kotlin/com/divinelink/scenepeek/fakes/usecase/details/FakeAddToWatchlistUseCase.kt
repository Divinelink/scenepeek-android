package com.divinelink.scenepeek.fakes.usecase.details

import com.divinelink.feature.details.media.usecase.AddToWatchlistUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeAddToWatchlistUseCase {

  val mock: AddToWatchlistUseCase = mock()

  suspend fun mockAddToWatchlist(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
