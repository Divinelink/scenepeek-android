package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.session.LogoutUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeLogoutUseCase {

  val mock: LogoutUseCase = mock()

  suspend fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(Result.failure(Exception()))
  }

  suspend fun mockSuccess(response: Result<Unit>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
