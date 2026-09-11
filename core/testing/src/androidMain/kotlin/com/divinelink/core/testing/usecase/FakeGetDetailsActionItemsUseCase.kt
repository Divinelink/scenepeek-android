package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.GetDetailsActionItemsUseCase
import com.divinelink.core.model.details.DetailActionItem
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetDetailsActionItemsUseCase {

  val mock: GetDetailsActionItemsUseCase = mock()

  suspend fun mockFailure() {
    whenever(mock.invoke(any())).thenReturn(Result.failure(Exception()))
  }

  suspend fun mockSuccess(response: Result<List<DetailActionItem>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
