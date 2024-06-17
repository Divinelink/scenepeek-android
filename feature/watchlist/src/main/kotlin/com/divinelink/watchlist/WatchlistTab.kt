package com.divinelink.watchlist

import androidx.annotation.StringRes
import com.divinelink.core.model.media.MediaType

enum class WatchlistTab(@StringRes val titleRes: Int, val value: String) {
  MOVIE(R.string.movie_tab, MediaType.MOVIE.value),
  TV(R.string.tv_show_tab, MediaType.TV.value),
}
