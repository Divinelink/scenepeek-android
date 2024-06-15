package com.divinelink.watchlist

import com.divinelink.core.model.media.MediaItem

sealed interface WatchlistForm<out T : MediaItem.Media> {
  data object Loading : WatchlistForm<Nothing>
  data class Data<T : MediaItem.Media>(val data: List<T>) : WatchlistForm<T>
}
