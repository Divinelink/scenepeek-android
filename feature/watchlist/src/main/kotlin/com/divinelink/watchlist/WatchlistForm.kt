package com.divinelink.watchlist

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.UIText

sealed interface WatchlistForm<out T : MediaItem.Media> {
  data object Loading : WatchlistForm<Nothing>

  data class Data<T : MediaItem.Media>(
    val data: List<T>,
    val totalResults: Int
  ) : WatchlistForm<T> {
    private val isMovie = data.firstOrNull()?.mediaType == MediaType.MOVIE
    private val isTvShow = data.firstOrNull()?.mediaType == MediaType.TV

    val totalResultsUiText: UIText? = if (isMovie) {
      UIText.ResourceText(R.string.total_movies_in_watchlist, totalResults)
    } else if (isTvShow) {
      UIText.ResourceText(R.string.total_tv_shows_in_watchlist, totalResults)
    } else {
      null
    }
  }
}
