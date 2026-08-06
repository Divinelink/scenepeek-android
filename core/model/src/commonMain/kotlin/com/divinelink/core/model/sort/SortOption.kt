package com.divinelink.core.model.sort

data class SortOption(
  val sortBy: SortBy,
  val direction: SortDirection,
) {
  val sortValue: String = "${sortBy.value}.${direction.value}"

  companion object {
    val defaultDiscoverSortOption = SortOption(
      sortBy = SortBy.POPULARITY,
      direction = SortDirection.DESC,
    )
    val defaultUserDataSortOption = SortOption(
      sortBy = SortBy.CREATED_AT,
      direction = SortDirection.DESC,
    )
  }
}
