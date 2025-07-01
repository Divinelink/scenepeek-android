package com.divinelink.core.network.account.mapper

import com.divinelink.core.model.PaginationData
import com.divinelink.core.network.account.model.ListsResponse

fun ListsResponse.map(): PaginationData<Unit> = PaginationData(
  page = page,
  totalPages = totalPages,
  totalResults = totalResults,
  list = emptyList(),
)
