package com.divinelink.core.model.sort

data class SortOption(
  val sortBy: SortBy,
  val direction: SortDirection,
) {
  val sortValue: String = "${sortBy.value}.${direction.value}"
}
