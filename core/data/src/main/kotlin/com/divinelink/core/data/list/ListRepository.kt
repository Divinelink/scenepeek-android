package com.divinelink.core.data.list

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.network.Resource
import com.divinelink.core.network.list.model.create.CreateListRequest
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.network.list.model.update.UpdateListResponse
import kotlinx.coroutines.flow.Flow

interface ListRepository {

  suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResult>

  suspend fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Flow<Resource<ListDetails?>>

  suspend fun createList(request: CreateListRequest): Result<CreateListResult>

  suspend fun fetchListsBackdrops(listId: Int): Flow<Map<String, String>>

  suspend fun fetchUserLists(
    accountId: String,
    page: Int,
  ): Flow<Resource<PaginationData<ListItem>?>>

  suspend fun deleteList(listId: Int): Result<Unit>

  suspend fun updateList(
    listId: Int,
    request: UpdateListRequest,
  ): Result<UpdateListResponse>

  suspend fun removeItems(
    listId: Int,
    items: List<MediaReference>,
  ): Result<Unit>
}
