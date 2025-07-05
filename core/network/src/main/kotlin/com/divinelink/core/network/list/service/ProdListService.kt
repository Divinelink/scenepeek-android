package com.divinelink.core.network.list.service

import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.list.model.AddToListRequest
import com.divinelink.core.network.list.model.AddToListResponse
import com.divinelink.core.network.list.model.MediaItemRequest
import com.divinelink.core.network.list.util.buildAddItemsToListUrl

class ProdListService(private val client: AuthTMDbClient) : ListService {

  override suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResponse> = runCatching {
    client.post<AddToListRequest, AddToListResponse>(
      url = buildAddItemsToListUrl(listId),
      body = AddToListRequest(
        listOf(
          MediaItemRequest(
            mediaId = mediaId,
            mediaType = mediaType,
          ),
        ),
      ),
    )
  }
}
