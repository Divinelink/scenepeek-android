package com.andreolas.movierama.fakes.usecase.settings.app.account

import com.andreolas.movierama.session.model.AccountDetails
import com.andreolas.movierama.settings.app.account.usecase.GetAccountDetailsUseCase
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetAccountDetailsUseCase {

  val mock: GetAccountDetailsUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure() {
    whenever(
      mock.invoke(any())
    ).thenReturn(
      flowOf(Result.failure(Exception()))
    )
  }

  fun mockSuccess(
    response: Result<AccountDetails>,
  ) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
