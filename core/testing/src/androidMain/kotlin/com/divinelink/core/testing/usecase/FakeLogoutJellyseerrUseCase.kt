package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.LogoutJellyseerrUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class FakeLogoutJellyseerrUseCase {

  val mock: LogoutJellyseerrUseCase = mock()

  suspend fun mockFailure(error: Throwable = Exception()) {
    whenever(mock.invoke(any())).thenReturn(Result.failure(error))
  }

  suspend fun mockSuccess(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }

  suspend fun verifyLogoutInteraction() {
    verify(mock).invoke(Unit)
  }
}
