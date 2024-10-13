package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class JellyseerrSettingsUiState(
  val snackbarMessage: SnackbarMessage?,
  val jellyseerrState: JellyseerrState,
) {
  companion object {
    fun initial(): JellyseerrSettingsUiState = JellyseerrSettingsUiState(
      snackbarMessage = null,
      jellyseerrState = JellyseerrState.Initial(
        address = "",
        isLoading = false,
      ),
    )
  }
}
