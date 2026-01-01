package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.session.ObserveAccountUseCase
import com.divinelink.core.model.account.TMDBAccount
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestObserveAccountUseCase {

  val mock: ObserveAccountUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure(exception: Exception = Exception()) {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(exception)))
  }

  fun mockResponse(response: Result<TMDBAccount>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
