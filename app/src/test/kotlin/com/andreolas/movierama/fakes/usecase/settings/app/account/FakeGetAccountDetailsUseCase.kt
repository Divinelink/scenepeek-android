package com.andreolas.movierama.fakes.usecase.settings.app.account

import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.model.account.AccountDetails
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
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(Exception())),
    )
  }

  fun mockSuccess(response: Result<AccountDetails>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
