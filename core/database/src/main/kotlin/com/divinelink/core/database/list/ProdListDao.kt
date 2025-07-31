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
import com.divinelink.core.model.media.MediaReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ProdListDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
) : ListDao {

  /**
   * List Details
   */
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
          mediaType = mediaItem.mediaType.value,
          itemOrder = (page - 1) * 20L + index.toLong(),
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

      detailsEntity
        .combine(listMedia) { details, media ->
          details?.copy(
            page = page,
            media = media,
          )
        }
        .distinctUntilChanged()
    }
  }

  override fun insertMediaToList(
    listId: Int,
    mediaType: String,
    mediaId: Int,
  ) = database.transaction {
    val itemExists = database.listMediaItemEntityQueries
      .checkIfItemExistsInList(
        listId = listId.toLong(),
        mediaItemId = mediaId.toLong(),
        mediaType = mediaType,
      )
      .executeAsOneOrNull() != null

    if (!itemExists) {
      database.listMediaItemEntityQueries.insertListMediaItemAtBottom(
        listId = listId.toLong(),
        listId_ = listId.toLong(),
        mediaType = mediaType,
        mediaItemId = mediaId.toLong(),
      )

      database.listItemEntityQueries.increaseListItemCount(
        id = listId.toLong(),
      )
    }
  }

  override fun removeMediaFromList(
    listId: Int,
    items: List<MediaReference>,
  ) = database.transaction {
    // Delete all items first
    items.forEach { media ->
      database.listMediaItemEntityQueries.deleteMediaFromList(
        listId = listId.toLong(),
        mediaItemId = media.mediaId.toLong(),
        mediaType = media.mediaType.value,
      )
    }

    // Get remaining items and reassign consecutive orders
    val remainingItems = database.listMediaItemEntityQueries
      .getRemainingItemsInOrder(listId.toLong())
      .executeAsList()

    remainingItems.forEachIndexed { index, item ->
      database.listMediaItemEntityQueries.updateItemOrder(
        itemOrder = index.toLong(),
        listId = item.listId,
        mediaItemId = item.mediaItemId,
        mediaType = item.mediaType,
      )
    }

    database.listItemEntityQueries.decreaseListItemCountBy(
      id = listId.toLong(),
      size = items.size.toLong(),
    )
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
      value_ = (page - 1) * 20L,
    )
    .asFlow()
    .mapToList(context = dispatcher.io)
    .map {
      it.mapNotNull { entity ->
        database
          .mediaItemEntityQueries
          .selectMediaItemByIdAndType(entity.mediaItemId, entity.mediaType)
          .executeAsOneOrNull()
          ?.map()
      }
    }

  /**
   * End of list details
   */

  override fun insertListItem(
    page: Int,
    accountId: String,
    items: List<ListItem>,
  ) = database.transaction {
    (0..<20).forEach { index ->
      val listItem = items.getOrNull(index)
      if (listItem != null) {
        database.listItemEntityQueries.insertListItem(
          ListItemEntity = listItem.map(
            accountId = accountId,
            index = (page - 1) * 20 + index.toLong(),
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

  override fun insertAtTheTopOfList(
    accountId: String,
    item: ListItem,
  ) = database.transaction {
    database.listItemEntityQueries.shiftItemsToNegative(accountId)
    database.listItemEntityQueries.flipNegativeIndices(accountId)

    database.listItemEntityQueries.insertListItem(
      ListItemEntity = item.map(
        accountId = accountId,
        index = 0L,
      ),
    )
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

    database.listItemEntityQueries.updateListItem(
      id = listId.toLong(),
      name = name,
      description = description,
      backdropPath = backdropPath,
      isPublic = if (isPublic) 1 else 0,
    )
  }

  override fun fetchListsBackdrops(listId: Int): Flow<Map<String, String>> = database
    .transactionWithResult {
      database
        .listMediaItemEntityQueries
        .fetchAllBackdropPathsByListId(listId = listId.toLong())
        .asFlow()
        .mapToList(dispatcher.io)
        .map { list ->
          list
            .filter { !it.backdropPath.isNullOrEmpty() && !it.name.isNullOrEmpty() }
            .associate {
              it.name!! to it.backdropPath!!
            }
        }
    }
}
