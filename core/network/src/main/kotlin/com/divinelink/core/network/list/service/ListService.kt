package com.divinelink.core.network.list.service

import com.divinelink.core.network.account.model.ListsResponse
import com.divinelink.core.network.list.model.CreateListRequest
import com.divinelink.core.network.list.model.CreateListResponse
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
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
}
