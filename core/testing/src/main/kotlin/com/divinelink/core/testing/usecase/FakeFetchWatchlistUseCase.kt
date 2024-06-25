package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.FetchWatchlistUseCase
import com.divinelink.core.model.watchlist.WatchlistResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeFetchWatchlistUseCase {

  val mock: FetchWatchlistUseCase = mock()

  /**
   * Response is a failure by default.
   */
  init {
    mockFailure()
  }

  private fun mockFailure() {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(Exception())),
    )
  }

  fun mockSuccess(response: Result<WatchlistResponse>) {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockSuccess(response: Flow<Result<WatchlistResponse>>) {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      response,
    )
  }
}