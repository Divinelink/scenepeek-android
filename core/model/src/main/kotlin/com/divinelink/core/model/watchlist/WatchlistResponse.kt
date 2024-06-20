package com.divinelink.core.model.watchlist

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class WatchlistResponse(
  val data: List<MediaItem.Media>,
  val totalResults: Int,
  val type: MediaType,
  val canFetchMore: Boolean,
)
