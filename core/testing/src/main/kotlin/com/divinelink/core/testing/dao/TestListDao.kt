package com.divinelink.core.testing.dao

import com.divinelink.core.database.list.ListDao
import com.divinelink.core.database.list.ListMetadata
import com.divinelink.core.model.list.ListItem
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestListDao {

  val mock: ListDao = mock()

  fun mockFetchListsMetadata(metadata: ListMetadata?) {
    whenever(
      mock.fetchListsMetadata(any()),
    ).thenReturn(
      metadata,
    )
  }

  fun mockFetchUserLists(items: List<ListItem>) {
    whenever(
      mock.fetchUserLists(
        accountId = any(),
        fromIndex = any(),
      ),
    ).thenReturn(
      flowOf(items),
    )
  }
}
