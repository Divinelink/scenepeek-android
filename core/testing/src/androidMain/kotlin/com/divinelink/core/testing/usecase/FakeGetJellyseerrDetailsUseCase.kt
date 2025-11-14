package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.GetJellyseerrProfileUseCase
import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FakeGetJellyseerrDetailsUseCase {

  val mock: GetJellyseerrProfileUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(Exception())))
  }

  fun mockSuccess(response: Result<JellyseerrProfileResult>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }

  fun mockSuccess(response: Flow<Result<JellyseerrProfileResult>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }

  fun mockSuccess(response: Channel<Result<JellyseerrProfileResult>>) {
    whenever(mock.invoke(any())).thenReturn(response.consumeAsFlow())
  }

  fun verifyInteractions(number: Int) {
    verify(mock, times(number)).invoke(any())
  }
}
