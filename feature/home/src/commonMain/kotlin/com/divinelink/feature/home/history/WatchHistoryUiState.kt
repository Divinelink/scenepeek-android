package com.divinelink.feature.home.history

import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class WatchHistoryUiState(
  val history: List<WatchedEpisode>,
  val offset: Long,
  val loading: Boolean,
  val snackbarMessage: SnackbarMessage?,
) {
  companion object {
    val initial = WatchHistoryUiState(
      history = emptyList(),
      offset = 0,
      loading = true,
      snackbarMessage = null,
    )
  }
}
