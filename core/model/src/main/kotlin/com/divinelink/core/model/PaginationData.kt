package com.divinelink.core.model

data class PaginationData<T>(
  val page: Int,
  val totalPages: Int,
  val totalResults: Int,
  val list: List<T>,
) {
  fun canLoadMore(): Boolean = page < totalPages
}
