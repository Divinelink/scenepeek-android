package com.divinelink.core.testing.repository

import com.divinelink.core.data.list.ListRepository
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestListRepository {
  val mock: ListRepository = mock()

  suspend fun mockFetchUserLists(response: Flow<Resource<PaginationData<ListItem>?>>) {
    whenever(
      mock.fetchUserLists(accountId = any(), page = any()),
    ).thenReturn(response)
  }

  suspend fun mockAddItemToList(result: Result<AddToListResult>) {
    whenever(
      mock.addItemToList(
        any(),
        any(),
        any(),
      ),
    ).thenReturn(result)
  }

  suspend fun mockCreateList(result: Result<CreateListResult>) {
    whenever(
      mock.createList(any()),
    ).thenReturn(result)
  }
}
