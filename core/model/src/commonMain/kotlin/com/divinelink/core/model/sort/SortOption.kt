package com.divinelink.core.model.sort

data class SortOption(
  val sortBy: SortBy,
  val direction: SortDirection,
) {
  val sortValue: String = "${sortBy.value}.${direction.value}"

  companion object {
    fun default(sort: SortContext) = SortOption(
      sortBy = sort.defaultOption,
      direction = SortDirection.DESC,
    )

    fun from(value: String?): SortOption? {
      if (value == null) return null

      val parts = value.split(".")
      if (parts.size != 2) return null

      val sortBy = SortBy.from(parts[0]) ?: return null
      val direction = SortDirection.from(parts[1])

      return SortOption(sortBy, direction)
    }
  }
}
