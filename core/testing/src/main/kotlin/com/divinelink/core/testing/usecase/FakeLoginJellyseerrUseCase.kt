package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import com.divinelink.core.model.jellyseerr.JellyseerrAccountStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeLoginJellyseerrUseCase {

  val mock: LoginJellyseerrUseCase = mock()

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

  fun mockSuccess(response: Flow<Result<JellyseerrAccountStatus>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
