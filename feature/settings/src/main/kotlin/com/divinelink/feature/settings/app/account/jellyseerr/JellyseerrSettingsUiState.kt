package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.components.dialog.DialogState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class JellyseerrSettingsUiState(
  val snackbarMessage: SnackbarMessage?,
  val dialogState: DialogState?,
  val jellyseerrState: JellyseerrState,
) {
  companion object {
    fun initial(): JellyseerrSettingsUiState = JellyseerrSettingsUiState(
      snackbarMessage = null,
      dialogState = null,
      jellyseerrState = JellyseerrState.Login(
        isLoading = false,
      ),
    )
  }
}
