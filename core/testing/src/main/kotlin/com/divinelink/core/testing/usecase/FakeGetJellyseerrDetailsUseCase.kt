package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.GetJellyseerrDetailsUseCase
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetJellyseerrDetailsUseCase {

  val mock: GetJellyseerrDetailsUseCase = mock()

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

  fun mockSuccess(response: Result<JellyseerrAccountStatus>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
