package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.GetDetailsActionItemsUseCase
import com.divinelink.core.model.details.DetailActionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetDetailsActionItemsUseCase {

  val mock: GetDetailsActionItemsUseCase = mock()

  init {
    mockFailure()
  }

  fun mockFailure() {
    whenever(
      mock.invoke(any()),
    ).thenReturn(
      flowOf(Result.failure(Exception())),
    )
  }

  fun mockSuccess(response: Flow<Result<List<DetailActionItem>>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
