package com.divinelink.core.model.watchlist

import com.divinelink.core.model.media.MediaType

data class WatchlistParameters(
  val page: Int,
  val sortBy: WatchlistSorting = WatchlistSorting.DESCENDING,
  val mediaType: MediaType,
)
