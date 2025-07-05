package com.divinelink.core.network.list.service

import com.divinelink.core.network.list.model.AddToListResponse

interface ListService {
  suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResponse>
}
