package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.list.AddItemToListUseCase
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestAddItemToListUseCase {

  val mock: AddItemToListUseCase = mock()

  init {
    mockFailure()
  }

  private fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(flowOf(Result.failure(Exception())))
  }

  fun mockResponse(response: Result<Boolean>) {
    whenever(mock.invoke(any())).thenReturn(flowOf(response))
  }
}
