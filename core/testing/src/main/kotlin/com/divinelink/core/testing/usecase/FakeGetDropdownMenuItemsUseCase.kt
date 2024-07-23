package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.GetDropdownMenuItemsUseCase
import com.divinelink.core.model.details.DetailsMenuOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeGetDropdownMenuItemsUseCase {

  val mock: GetDropdownMenuItemsUseCase = mock()

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

  fun mockSuccess(response: Flow<Result<List<DetailsMenuOptions>>>) {
    whenever(mock.invoke(any())).thenReturn(response)
  }
}
