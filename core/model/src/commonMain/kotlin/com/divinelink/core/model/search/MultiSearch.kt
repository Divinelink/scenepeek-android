package com.divinelink.core.model.search

import com.divinelink.core.model.media.MediaItem

data class MultiSearch(
  val searchList: List<MediaItem>,
  val totalPages: Int,
)
