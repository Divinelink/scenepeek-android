package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.jellyseerr.LoginJellyseerrUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeLoginJellyseerrUseCase {

  val mock: LoginJellyseerrUseCase = mock()

  init {
    mockFailure(Exception())
  }

  fun mockFailure(throwable: Throwable) {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(throwable)),
    )
  }

  fun mockSuccess(response: Flow<Result<Unit>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
