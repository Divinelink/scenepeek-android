package com.divinelink.watchlist

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class WatchlistUiState(
  val selectedTab: Int,
  val tabs: List<WatchlistTab>,
  val pages: Map<MediaType, Int>,
  val forms: Map<MediaType, WatchlistForm<MediaItem.Media>>
) {
  private val tvForm = forms[MediaType.TV]
  val tvFormIsLoading = tvForm is WatchlistForm.Loading

  val tvPage = pages[MediaType.TV] ?: 1
  val moviesPage = pages[MediaType.MOVIE] ?: 1
}
