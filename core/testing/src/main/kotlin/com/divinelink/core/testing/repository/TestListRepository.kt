package com.divinelink.core.testing.repository

import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.model.list.AddToListResult
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestListRepository {
  val mock: ListRepository = mock()

  suspend fun mockAddItemToList(result: Result<AddToListResult>) {
    whenever(
      mock.addItemToList(
        any(),
        any(),
        any(),
      ),
    ).thenReturn(result)
  }
}
