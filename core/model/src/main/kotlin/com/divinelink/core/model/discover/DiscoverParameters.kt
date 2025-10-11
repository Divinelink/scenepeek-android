package com.divinelink.core.model.discover

import com.divinelink.core.model.media.MediaType

data class DiscoverParameters(
  val page: Int,
  val mediaType: MediaType,
  val filters: List<DiscoverFilter>,
)
