package com.divinelink.core.data.list

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.network.account.mapper.map
import com.divinelink.core.network.list.mapper.add.map
import com.divinelink.core.network.list.mapper.details.map
import com.divinelink.core.network.list.model.CreateListRequest
import com.divinelink.core.network.list.service.ListService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProdListRepository(private val service: ListService) : ListRepository {

  override suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResult> = service.addItemToList(
    listId = listId,
    mediaId = mediaId,
    mediaType = mediaType,
  ).map { it.map() }

  override suspend fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Result<ListDetails> = service.fetchListDetails(
    listId = listId,
    page = page,
  ).map { it.map() }

  override suspend fun createList(request: CreateListRequest): Result<CreateListResult> = service
    .createList(request)
    .map {
      CreateListResult(
        id = it.id,
        success = it.success,
      )
    }

  override suspend fun fetchUserLists(
    accountId: String,
    page: Int,
  ): Flow<Result<PaginationData<ListItem>>> = service
    .fetchUserLists(
      accountId = accountId,
      page = page,
    )
    .map { Result.success(it.map()) }
}
