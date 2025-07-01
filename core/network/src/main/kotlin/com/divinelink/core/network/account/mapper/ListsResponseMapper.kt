package com.divinelink.core.network.account.mapper

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.network.account.model.ListsResponse

fun ListsResponse.map(): PaginationData<ListItem> = PaginationData(
  page = page,
  totalPages = totalPages,
  totalResults = totalResults,
  list = results.map { it.map() },
)

fun ListsResponse.ListItemResponse.map() = ListItem(
  name = name,
  posterPath = posterPath,
  backdropPath = backdropPath,
  description = description,
  public = public == 1,
)
