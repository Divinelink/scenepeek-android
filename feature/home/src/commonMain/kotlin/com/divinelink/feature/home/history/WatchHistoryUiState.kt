package com.divinelink.feature.home.history

import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.ui.snackbar.SnackbarMessage
import kotlinx.datetime.LocalDate

data class WatchHistoryUiState(
  val history: Map<LocalDate, List<WatchedEpisode>>,
  val offset: Long,
  val loading: Boolean,
  val snackbarMessage: SnackbarMessage?,
) {
  companion object {
    val initial = WatchHistoryUiState(
      history = emptyMap(),
      offset = 0,
      loading = true,
      snackbarMessage = null,
    )
  }
}
