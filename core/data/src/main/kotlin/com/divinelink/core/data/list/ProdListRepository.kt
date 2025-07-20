package com.divinelink.core.data.list

import com.divinelink.core.database.list.ListDao
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.network.Resource
import com.divinelink.core.network.account.mapper.map
import com.divinelink.core.network.list.mapper.add.map
import com.divinelink.core.network.list.mapper.details.map
import com.divinelink.core.network.list.model.CreateListRequest
import com.divinelink.core.network.list.service.ListService
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class ProdListRepository(
  private val dao: ListDao,
  private val service: ListService,
) : ListRepository {

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
  ): Flow<Resource<PaginationData<ListItem>?>> = networkBoundResource(
    query = {
      val metadata = dao.fetchListsMetadata(accountId)
      val lists = dao.fetchUserLists(accountId, fromIndex = (page - 1) * 20)

      combine(
        flowOf(metadata),
        lists,
      ) { data, cachedLists ->
        if (data == null) {
          null
        } else {
          PaginationData(
            page = page,
            list = cachedLists,
            totalPages = data.totalPages.toInt(),
            totalResults = data.totalResults.toInt(),
          )
        }
      }
    },
    fetch = {
      service.fetchUserLists(accountId, page).first()
    },
    saveFetchResult = { remoteData ->
      dao.insertListMetadata(
        accountId = accountId,
        totalPages = remoteData.totalPages,
        totalResults = remoteData.totalResults,
      )
      dao.insertListItem(
        page = page,
        accountId = accountId,
        items = remoteData.map().list,
      )
    },
  )
}
