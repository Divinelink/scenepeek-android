package com.divinelink.core.network.list.service

import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.list.model.MediaItemRequest
import com.divinelink.core.network.list.model.add.AddToListRequest
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.list.util.buildAddItemsToListUrl
import com.divinelink.core.network.list.util.buildFetchListDetailsUrl

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

  override suspend fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Result<ListDetailsResponse> = runCatching {
    client.get<ListDetailsResponse>(
      url = buildFetchListDetailsUrl(
        listId = listId,
        page = page,
      ),
    )
  }
}
