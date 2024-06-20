package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.session.ObserveSessionUseCase
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeObserveSessionUseCase {

  val mock: ObserveSessionUseCase = mock()

  init {
    mockFailure()
  }

  fun mockFailure(exception: Exception = Exception()) {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(exception)),
    )
  }

  fun mockSuccess(response: Result<Boolean>) {
    whenever(mock.invoke(any())).thenReturn(
      flowOf(response),
    )
  }
}
