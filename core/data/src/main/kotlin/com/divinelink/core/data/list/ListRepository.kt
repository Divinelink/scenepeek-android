package com.divinelink.core.data.list

import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.network.list.model.CreateListRequest

interface ListRepository {

  suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResult>

  suspend fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Result<ListDetails>

  suspend fun createList(request: CreateListRequest): Result<CreateListResult>
}
