package com.divinelink.core.data.list

import com.divinelink.core.commons.domain.data
import com.divinelink.core.database.currentTimeInUTC
import com.divinelink.core.database.list.ListDao
import com.divinelink.core.database.media.dao.SqlMediaDao
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.AddToListResult
import com.divinelink.core.model.list.CreateListResult
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Resource
import com.divinelink.core.network.account.mapper.map
import com.divinelink.core.network.list.mapper.add.map
import com.divinelink.core.network.list.mapper.details.map
import com.divinelink.core.network.list.model.create.CreateListRequest
import com.divinelink.core.network.list.model.update.UpdateListRequest
import com.divinelink.core.network.list.model.update.UpdateListResponse
import com.divinelink.core.network.list.service.ListService
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock

class ProdListRepository(
  private val sessionStorage: SessionStorage,
  private val listDao: ListDao,
  private val mediaDao: SqlMediaDao,
  private val service: ListService,
  private val clock: Clock,
) : ListRepository {

  override suspend fun addItemToList(
    listId: Int,
    mediaId: Int,
    mediaType: String,
  ): Result<AddToListResult> = service.addItemToList(
    listId = listId,
    mediaId = mediaId,
    mediaType = mediaType,
  )
    .map { it.map() }
    .also {
      if (it.data is AddToListResult.Success) {
        listDao.insertMediaToList(
          listId = listId,
          mediaType = mediaType,
          mediaId = mediaId,
        )
      }
    }

  override suspend fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Flow<Resource<ListDetails?>> = networkBoundResource(
    query = {
      listDao.fetchListDetails(
        listId = listId,
        page = page,
      )
    },
    fetch = {
      service.fetchListDetails(listId, page)
    },
    saveFetchResult = { remoteData ->
      val details = remoteData.data.map()

      mediaDao.insertMedia(details.media)
      listDao.insertListDetails(
        page = page,
        details = details,
      )
    },
  )

  override suspend fun createList(request: CreateListRequest): Result<CreateListResult> = service
    .createList(request)
    .map { response ->
      if (response.success) {
        listDao.insertAtTheTopOfList(
          accountId = sessionStorage.accountId ?: throw SessionException.Unauthenticated(),
          item = ListItem(
            id = response.id,
            name = request.name,
            description = request.description,
            backdropPath = null,
            posterPath = null,
            public = request.public == 1,
            numberOfItems = 0,
            updatedAt = clock.currentTimeInUTC(),
          ),
        )
      }

      CreateListResult(
        id = response.id,
        success = response.success,
      )
    }

  override suspend fun fetchUserLists(
    accountId: String,
    page: Int,
  ): Flow<Resource<PaginationData<ListItem>?>> = networkBoundResource(
    query = {
      val metadata = listDao.fetchListsMetadata(accountId)
      val lists = listDao.fetchUserLists(accountId, fromIndex = (page - 1) * 20)

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
      listDao.insertListMetadata(
        accountId = accountId,
        totalPages = remoteData.totalPages,
        totalResults = remoteData.totalResults,
      )
      listDao.insertListItem(
        page = page,
        accountId = accountId,
        items = remoteData.map().list,
      )
    },
  )

  override suspend fun deleteList(listId: Int): Result<Unit> = service
    .deleteList(listId)
    .onSuccess {
      listDao.deleteList(listId)
    }

  override suspend fun updateList(
    listId: Int,
    request: UpdateListRequest,
  ): Result<UpdateListResponse> = service
    .updateList(
      listId = listId,
      request = request,
    )
    .onSuccess {
      if (it.success) {
        listDao.updateList(
          listId = listId,
          name = request.name,
          description = request.description,
          backdropPath = request.backdropPath ?: "",
          isPublic = request.public,
        )
      }
    }

  override suspend fun fetchListsBackdrops(listId: Int): Flow<Map<String, String>> = listDao
    .fetchListsBackdrops(listId)

  override suspend fun removeItems(
    listId: Int,
    items: List<MediaReference>,
  ): Result<Unit> = service
    .removeItems(
      listId = listId,
      items = items,
    )
    .map { response ->
      val successfullyRemoved = response.results
        .filter { it.success }
        .map { MediaReference(it.mediaId, mediaType = MediaType.from(it.mediaType)) }

      listDao.removeMediaFromList(listId, successfullyRemoved)
    }
}
