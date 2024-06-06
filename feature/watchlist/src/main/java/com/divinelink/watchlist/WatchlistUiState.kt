package com.divinelink.watchlist

import com.divinelink.core.model.media.MediaItem

sealed class Tab<out T> {
  data object Loading : Tab<Nothing>()
  data class Data<T>(val data: List<T>) : Tab<T>()
}

data class WatchlistUiState(
  val tabs: List<WatchlistTab>,
  val movies: Tab<MediaItem.Media.Movie>,
  val tvShows: Tab<MediaItem.Media.TV>
)
