package com.divinelink.core.network.list.service

import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.network.account.model.ListsResponse
import com.divinelink.core.network.account.util.buildFetchListsUrl
import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.list.model.MediaItemRequest
import com.divinelink.core.network.list.model.add.AddToListRequest
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.create.CreateListRequest
import com.divinelink.core.network.list.model.create.CreateListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.list.model.remove.RemoveItemsRequest
import com.divinelink.core.network.list.model.remove.RemoveItemsResponse
import com.divinelink.core.network.list.model.status.ItemStatusResponse
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.network.list.model.update.UpdateListResponse
import com.divinelink.core.network.list.util.buildFetchListDetailsUrl
import com.divinelink.core.network.list.util.buildListItemStatusUrl
import com.divinelink.core.network.list.util.buildListItemsUrl
import com.divinelink.core.network.list.util.buildListUrl
import com.divinelink.core.network.list.util.buildListWithIdUrl
import com.divinelink.core.network.runCatchingWithNetworkRetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdListService(private val client: AuthTMDbClient) : ListService {

  override suspend fun addItemToList(
    listId: Int,
    media: MediaReference,
  ): Result<AddToListResponse> = runCatchingWithNetworkRetry(
    times = 10,
    maxDelay = 1000L,
  ) {
    client.post<AddToListRequest, AddToListResponse>(
      url = buildListItemsUrl(listId),
      body = AddToListRequest(
        listOf(
          MediaItemRequest(
            mediaId = media.mediaId,
            mediaType = media.mediaType.value,
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
        language = client.metadataLanguage(),
      ),
    )
  }

  override fun fetchUserLists(
    accountId: String,
    page: Int,
  ): Flow<ListsResponse> {
    val url = buildFetchListsUrl(
      accountId = accountId,
      page = page,
    )

    return flow {
      emit(client.get<ListsResponse>(url = url))
    }
  }

  override suspend fun createList(request: CreateListRequest): Result<CreateListResponse> =
    runCatching {
      val url = buildListUrl()

      client.post<CreateListRequest, CreateListResponse>(
        url = url,
        body = request,
      )
    }

  override suspend fun deleteList(listId: Int): Result<Unit> = runCatching {
    val url = buildListWithIdUrl(listId)

    client.delete<Unit>(url)
  }

  override suspend fun updateList(
    listId: Int,
    request: UpdateListRequest,
  ): Result<UpdateListResponse> = runCatching {
    val url = buildListWithIdUrl(listId)

    client.put<UpdateListRequest, UpdateListResponse>(url, request)
  }

  override suspend fun removeItems(
    listId: Int,
    items: List<MediaReference>,
  ): Result<RemoveItemsResponse> = runCatching {
    val url = buildListItemsUrl(listId)

    client.delete<RemoveItemsRequest, RemoveItemsResponse>(
      url = url,
      body = RemoveItemsRequest.fromMediaReference(items),
    )
  }

  override suspend fun checkItemStatus(
    listId: Int,
    item: MediaReference,
  ): Result<ItemStatusResponse> = runCatching {
    val url = buildListItemStatusUrl(
      listId = listId,
      media = item,
    )

    client.get<ItemStatusResponse>(url = url)
  }
}
