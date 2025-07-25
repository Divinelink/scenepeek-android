package com.divinelink.core.data.list

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.network.Resource
import com.divinelink.core.network.list.model.CreateListRequest
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

  suspend fun fetchUserLists(
    accountId: String,
    page: Int,
  ): Flow<Resource<PaginationData<ListItem>?>>
}
