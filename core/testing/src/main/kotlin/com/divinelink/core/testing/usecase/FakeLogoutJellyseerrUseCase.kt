package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeLogoutJellyseerrUseCase {

  val mock: LogoutJellyseerrUseCase = mock()

  init {
    mockFailure()
  }

  fun mockFailure(error: Throwable = Exception()) {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(error)))
  }

  fun mockSuccess(response: Flow<Result<String>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
