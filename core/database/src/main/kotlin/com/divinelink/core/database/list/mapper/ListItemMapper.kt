package com.divinelink.core.database.list.mapper

import com.divinelink.core.database.list.ListItemEntity
import com.divinelink.core.model.list.ListItem

fun ListItem.map(
  accountId: String,
  index: Long,
) = ListItemEntity(
  id = id.toLong(),
  name = name,
  description = description,
  isPublic = if (public) 1 else 0,
  numberOfItems = numberOfItems.toLong(),
  posterPath = posterPath,
  backdropPath = backdropPath,
  accountId = accountId,
  updatedAt = updatedAt,
  itemIndex = index,
)
