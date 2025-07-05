package com.divinelink.core.testing.service

import com.divinelink.core.network.list.model.AddToListResponse
import com.divinelink.core.network.list.service.ListService
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestListService {
  val mock: ListService = mock()

  suspend fun mockAddItemToList(response: Result<AddToListResponse>) {
    whenever(mock.addItemToList(any(), any(), any())).thenReturn(
      response,
    )
  }
}
