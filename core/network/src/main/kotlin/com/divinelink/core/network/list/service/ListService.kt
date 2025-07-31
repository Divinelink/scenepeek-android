package com.divinelink.core.network.list.service

import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.network.account.model.ListsResponse
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.create.CreateListRequest
import com.divinelink.core.network.list.model.create.CreateListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.list.model.remove.RemoveItemsResponse
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.network.list.model.update.UpdateListResponse
import kotlinx.coroutines.flow.Flow

interface ListService {

  suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResponse>

  suspend fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Result<ListDetailsResponse>

  fun fetchUserLists(
    accountId: String,
    page: Int,
  ): Flow<ListsResponse>

  suspend fun createList(request: CreateListRequest): Result<CreateListResponse>

  suspend fun deleteList(listId: Int): Result<Unit>

  suspend fun updateList(
    listId: Int,
    request: UpdateListRequest,
  ): Result<UpdateListResponse>

  suspend fun removeItems(
    listId: Int,
    items: List<MediaReference>,
  ): Result<RemoveItemsResponse>
}
