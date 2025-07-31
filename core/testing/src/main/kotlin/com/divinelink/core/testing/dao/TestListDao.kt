package com.divinelink.core.testing.dao

import com.divinelink.core.database.list.ListDao
import com.divinelink.core.database.list.ListMetadataEntity
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class TestListDao {

  val mock: ListDao = mock()

  fun mockFetchListsMetadata(metadata: ListMetadataEntity?) {
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

  fun mockFetchListDetails(response: Flow<ListDetails?>) {
    whenever(
      mock.fetchListDetails(
        listId = any(),
        page = any(),
      ),
    ).thenReturn(response)
  }

  fun mockFetchListsBackdrops(
    listId: Int,
    backdrops: Map<String, String>,
  ) {
    whenever(mock.fetchListsBackdrops(listId)).thenReturn(flowOf(backdrops))
  }

  fun verifyMediaItemInserted(
    listId: Int,
    media: MediaReference,
  ) {
    verify(mock).insertMediaToList(listId, media)
  }

  fun verifyListDeleted(listId: Int) {
    verify(mock).deleteList(listId)
  }

  fun verifyInsertAtTheTopOfList(
    accountId: String,
    item: ListItem,
  ) {
    verify(mock).insertAtTheTopOfList(accountId, item)
  }

  fun verifyListUpdated(
    listId: Int,
    name: String,
    description: String,
    backdropPath: String,
    isPublic: Boolean,
  ) {
    verify(mock).updateList(
      listId = listId,
      name = name,
      description = description,
      backdropPath = backdropPath,
      isPublic = isPublic,
    )
  }

  fun verifyNoInteraction() {
    verifyNoInteractions(mock)
  }
}
