package com.divinelink.core.database.list

import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import kotlinx.coroutines.flow.Flow

interface ListDao {

  fun insertListDetails(
    page: Int,
    details: ListDetails,
  )

  fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Flow<ListDetails?>

  fun insertListItem(
    page: Int,
    accountId: String,
    items: List<ListItem>,
  )

  fun insertListMetadata(
    accountId: String,
    totalPages: Int,
    totalResults: Int,
  )

  fun fetchListsMetadata(accountId: String): ListMetadataEntity?

  fun fetchUserLists(
    accountId: String,
    fromIndex: Int,
  ): Flow<List<ListItem>>

  fun clearUserLists(accountId: String)

  fun deleteList(listId: Int)
}
