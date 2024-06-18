package com.divinelink.watchlist

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.ui.UIText
import com.divinelink.feature.watchlist.R

sealed interface WatchlistForm<out T : MediaItem.Media> {
  data object Loading : WatchlistForm<Nothing>

  sealed interface Error : WatchlistForm<Nothing> {
    data object Unauthenticated : Error
    data object Unknown : Error
  }

  data class Data<T : MediaItem.Media>(
    val data: List<T>,
    val totalResults: Int
  ) : WatchlistForm<T> {
    private val isMovie = data.firstOrNull()?.mediaType == MediaType.MOVIE
    private val isTvShow = data.firstOrNull()?.mediaType == MediaType.TV

    val totalResultsUiText: UIText? = if (isMovie) {
      UIText.ResourceText(R.string.feature_watchlist_total_movies_in_watchlist, totalResults)
    } else if (isTvShow) {
      UIText.ResourceText(R.string.feature_watchlist_total_tv_shows_in_watchlist, totalResults)
    } else {
      null
    }
  }
}
