package com.divinelink.core.network.list.service

import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse

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
}
