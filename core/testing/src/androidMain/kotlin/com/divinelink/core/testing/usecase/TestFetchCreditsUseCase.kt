package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.credits.FetchCreditsUseCase
import com.divinelink.core.model.credits.AggregateCredits
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestFetchCreditsUseCase {

  val mock: FetchCreditsUseCase = mock()

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

  fun mockSuccess(response: Flow<Result<AggregateCredits>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }

  // Mock multiple emissions
  fun mockSuccess(response: Channel<Result<AggregateCredits>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }
}
