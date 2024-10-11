package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.GetJellyseerrAccountDetailsUseCase
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetJellyseerrDetailsUseCase {

  val mock: GetJellyseerrAccountDetailsUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(Exception())))
  }

  fun mockSuccess(response: Result<JellyseerrAccountDetails?>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }

  fun mockSuccess(response: Channel<Result<JellyseerrAccountDetails?>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }
}
