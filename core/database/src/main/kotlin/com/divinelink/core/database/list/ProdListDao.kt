package com.divinelink.core.database.list

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.list.mapper.map
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.model.list.ListDetails
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ProdListDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
) : ListDao {

  override fun insertListDetails(
    page: Int,
    details: ListDetails,
  ) {
    database.transaction {
      database.listDetailsEntityQueries.insertListDetails(details.map())

      details.media.forEachIndexed { index, mediaItem ->
        database.listMediaItemEntityQueries.insertListMediaItem(
          listId = details.id.toLong(),
          mediaItemId = mediaItem.id.toLong(),
          itemOrder = (page - 1) * 20L + index.toLong(),
          page = page.toLong(),
        )
      }
    }
  }

  override fun fetchListDetails(
    listId: Int,
    page: Int,
  ): Flow<ListDetails?> = with(database) {
    transactionWithResult {
      val detailsEntity = fetchListDetails(id = listId.toLong())
      val listMedia = fetchMediaResultForList(id = listId.toLong(), page = page.toLong())

      combine(detailsEntity, listMedia) { details, media ->
        details?.copy(
          page = page,
          media = media,
        )
      }
    }
  }

  private fun fetchListDetails(id: Long): Flow<ListDetails?> = database
    .listDetailsEntityQueries
    .selectListDetailsById(id)
    .asFlow()
    .mapToOneOrNull(dispatcher.io)
    .map { entity ->
      entity.map()
    }

  private fun fetchMediaResultForList(
    id: Long,
    page: Long,
  ): Flow<List<MediaItem.Media>> = database
    .listMediaItemEntityQueries
    .fetchListMediaItemsByListId(
      listId = id,
      page = page,
    )
    .asFlow()
    .mapToList(context = dispatcher.io)
    .map {
      it.mapNotNull { entity ->
        database
          .mediaItemEntityQueries
          .selectMediaItemById(entity.mediaItemId)
          .executeAsOneOrNull()
          ?.map()
      }
    }

  override fun insertListItem(
    page: Int,
    accountId: String,
    items: List<ListItem>,
  ) = database.transaction {
    (0..<20).forEach { index ->
      val listItem = items.getOrNull(index)
      if (listItem != null) {
        database.listItemEntityQueries.insertListItem(
          ListItemEntity = ListItemEntity(
            id = listItem.id.toLong(),
            name = listItem.name,
            posterPath = listItem.posterPath,
            backdropPath = listItem.backdropPath,
            description = listItem.description,
            isPublic = if (listItem.public) 1 else 0,
            numberOfItems = listItem.numberOfItems.toLong(),
            page = page.toLong(),
            accountId = accountId,
            updatedAt = listItem.updatedAt,
            itemIndex = (page - 1) * 20 + index.toLong(),
          ),
        )
      } else {
        database.listItemEntityQueries.deleteItemsAfterIndex(
          accountId = accountId,
          startIndex = (page - 1) * 20 + index.toLong(),
        )
        return@transaction
      }
    }
  }

  override fun insertListMetadata(
    accountId: String,
    totalPages: Int,
    totalResults: Int,
  ) {
    database.listMetadataEntityQueries.insertListMetadata(
      accountId = accountId,
      totalPages = totalPages.toLong(),
      totalResults = totalResults.toLong(),
    )
  }

  override fun fetchListsMetadata(accountId: String): ListMetadataEntity? = database
    .transactionWithResult {
      database
        .listMetadataEntityQueries
        .fetchListMetadata(accountId)
        .executeAsOneOrNull()
    }

  override fun fetchUserLists(
    accountId: String,
    fromIndex: Int,
  ): Flow<List<ListItem>> = database.transactionWithResult {
    database
      .listItemEntityQueries
      .fetchUserLists(
        accountId = accountId,
        startIndex = fromIndex.toLong(),
        limit = 20,
      )
      .asFlow()
      .mapToList(dispatcher.io)
      .map {
        it.map { listItemEntity ->
          ListItem(
            id = listItemEntity.id.toInt(),
            name = listItemEntity.name,
            posterPath = listItemEntity.posterPath,
            backdropPath = listItemEntity.backdropPath,
            description = listItemEntity.description,
            public = listItemEntity.isPublic.toInt() == 1,
            numberOfItems = listItemEntity.numberOfItems.toInt(),
            updatedAt = listItemEntity.updatedAt,
          )
        }
      }
  }

  override fun clearUserLists(accountId: String) {
    database.transaction {
      database.listItemEntityQueries.clearListItems(accountId)
      database.listMetadataEntityQueries.clearListMetadata(accountId)
    }
  }

  override fun deleteList(listId: Int) = database.transaction {
    database.listItemEntityQueries.deleteList(listId.toLong())
    database.listDetailsEntityQueries.deleteListDetails(listId.toLong())
    database.listMediaItemEntityQueries.deleteListMediaItem(listId.toLong())
  }

  override fun updateList(
    listId: Int,
    name: String,
    description: String,
    backdropPath: String,
    isPublic: Boolean,
  ) = database.transaction {
    database.listDetailsEntityQueries.updateListDetails(
      id = listId.toLong(),
      name = name,
      description = description,
      backdropPath = backdropPath,
      isPublic = if (isPublic) 1 else 0,
    )
  }

  override fun fetchListsBackdrops(listId: Int): Flow<List<Pair<String, String>>> = database
    .transactionWithResult {
      database
        .listMediaItemEntityQueries
        .fetchAllBackdropPathsByListId(listId = listId.toLong())
        .asFlow()
        .mapToList(dispatcher.io)
        .map { list ->
          list.mapNotNull {
            if (it.backdropPath != null && it.name != null) {
              it.name to it.backdropPath
            } else {
              null
            }
          }
        }
    }
}
