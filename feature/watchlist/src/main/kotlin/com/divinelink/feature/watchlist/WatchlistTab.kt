package com.divinelink.feature.watchlist

import androidx.annotation.StringRes
import com.divinelink.core.model.media.MediaType

enum class WatchlistTab(
  @StringRes val titleRes: Int,
  val value: String,
) {
  MOVIE(R.string.feature_watchlist_movie_tab, MediaType.MOVIE.value),
  TV(R.string.feature_watchlist_tv_show_tab, MediaType.TV.value),
}
