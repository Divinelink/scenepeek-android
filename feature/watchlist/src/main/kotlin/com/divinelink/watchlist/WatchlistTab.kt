package com.divinelink.watchlist

import androidx.annotation.StringRes

enum class WatchlistTab(@StringRes val titleRes: Int) {
  MOVIE(R.string.movie_tab),
  TV_SHOW(R.string.tv_show_tab)
}
