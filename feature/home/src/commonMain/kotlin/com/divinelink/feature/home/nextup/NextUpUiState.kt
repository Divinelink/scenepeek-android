package com.divinelink.feature.home.nextup

import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.UIText
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.next_up_syncing_issue
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class NextUpUiState(
  val available: List<NextUpEpisode>,
  val upcoming: List<NextUpEpisode>,
  val paused: List<NextUpEpisode>,
  val errors: List<ShowSyncError>,
  val loading: Boolean,
  val snackbarMessage: SnackbarMessage?,
) {
  val displayMessage = if (errors.isNotEmpty() && !loading) {
    DisplayMessage.Warning(message = UIText.ResourceText(UiString.next_up_syncing_issue))
  } else {
    null
  }

  companion object {
    val initial = NextUpUiState(
      available = emptyList(),
      upcoming = emptyList(),
      paused = emptyList(),
      errors = emptyList(),
      loading = true,
      snackbarMessage = null,
    )
  }
}
