package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.list.CreateListUseCase
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestCreateListUseCase {

  val mock: CreateListUseCase = mock()

  fun mockFailure(error: Throwable = Exception()) {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(error)))
  }

  fun mockSuccess(response: Result<Int>) {
    whenever(mock(any())).thenReturn(flowOf(response))
  }
}
