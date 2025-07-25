package com.divinelink.core.network.list.service

import com.divinelink.core.network.account.model.ListsResponse
import com.divinelink.core.network.account.util.buildFetchListsUrl
import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.list.model.MediaItemRequest
import com.divinelink.core.network.list.model.add.AddToListRequest
import com.divinelink.core.network.list.model.add.AddToListResponse
import com.divinelink.core.network.list.model.create.CreateListRequest
import com.divinelink.core.network.list.model.create.CreateListResponse
import com.divinelink.core.network.list.model.details.ListDetailsResponse
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.network.list.model.update.UpdateListResponse
import com.divinelink.core.network.list.util.buildAddItemsToListUrl
import com.divinelink.core.network.list.util.buildFetchListDetailsUrl
import com.divinelink.core.network.list.util.buildListUrl
import com.divinelink.core.network.list.util.buildListWithIdUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
}
