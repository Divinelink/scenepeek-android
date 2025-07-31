package com.divinelink.core.testing.service

import com.divinelink.core.network.account.model.ListsResponse
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.create.CreateListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.network.list.model.update.UpdateListResponse
import com.divinelink.core.network.list.service.ListService
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestListService {
  val mock: ListService = mock()

  suspend fun mockAddItemToList(response: Result<AddToListResponse>) {
    whenever(mock.addItemToList(any(), any())).thenReturn(
      response,
    )
  }

  suspend fun mockFetchListDetails(response: Result<ListDetailsResponse>) {
    whenever(mock.fetchListDetails(any(), any())).thenReturn(response)
  }

  fun mockFetchUserLists(response: ListsResponse) {
    whenever(mock.fetchUserLists(any(), any())).thenReturn(flowOf(response))
  }

  fun mockFetchUserListsFailure(error: Throwable) {
    whenever(mock.fetchUserLists(any(), any())).thenThrow(error)
  }

  suspend fun mockCreateList(response: Result<CreateListResponse>) {
    whenever(mock.createList(any())).thenReturn(response)
  }

  suspend fun mockDeleteList(response: Result<Unit>) {
    whenever(mock.deleteList(any())).thenReturn(response)
  }

  suspend fun mockUpdateList(
    id: Int,
    request: UpdateListRequest,
    response: Result<UpdateListResponse>,
  ) {
    whenever(mock.updateList(id, request)).thenReturn(response)
  }
}
