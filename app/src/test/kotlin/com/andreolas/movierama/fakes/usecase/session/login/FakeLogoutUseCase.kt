package com.andreolas.movierama.fakes.usecase.session.login

import com.andreolas.movierama.session.login.domain.session.usecase.LogoutUseCase
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeLogoutUseCase {

  val mock: LogoutUseCase = mock()

  suspend fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(Result.failure(Exception()))
  }

  suspend fun mockSuccess(
    response: Result<Unit>,
  ) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
