package com.divinelink.feature.home.history

import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.ui.snackbar.SnackbarMessage
import kotlinx.datetime.LocalDate

data class WatchHistoryUiState(
  val history: Map<LocalDate, List<WatchedEpisode>>,
  val lastId: Long?,
  val canLoadMore: Boolean,
  val loading: Boolean,
  val initialFetch: Boolean,
  val snackbarMessage: SnackbarMessage?,
) {

  companion object {
    val initial = WatchHistoryUiState(
      history = emptyMap(),
      lastId = null,
      canLoadMore = true,
      loading = true,
      initialFetch = true,
      snackbarMessage = null,
    )
  }
}
