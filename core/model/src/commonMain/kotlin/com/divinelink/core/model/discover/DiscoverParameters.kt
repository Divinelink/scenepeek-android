package com.divinelink.core.model.discover

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption

data class DiscoverParameters(
  val page: Int,
  val mediaType: MediaType,
  val sortOption: SortOption,
  val filters: MediaTypeFilters,
)
