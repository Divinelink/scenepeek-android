package com.divinelink.core.database.list

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.model.list.ListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProdListDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
) : ListDao {

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
}
