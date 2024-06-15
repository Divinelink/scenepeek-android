package com.divinelink.watchlist

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class WatchlistUiState(
  val selectedTab: Int,
  val moviesPage: Int,
  val tvPage: Int,
  val tabs: List<WatchlistTab>,
  val forms: Map<MediaType, WatchlistForm<MediaItem.Media>>
)
