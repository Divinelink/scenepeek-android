package com.divinelink.feature.settings.app.account.jellyseerr

import com.divinelink.core.model.jellyseerr.SeerrState
import com.divinelink.core.ui.components.dialog.DialogState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class JellyseerrSettingsUiState(
  val snackbarMessage: SnackbarMessage?,
  val dialogState: DialogState?,
  val seerrState: SeerrState,
) {
  companion object {
    fun initial(): JellyseerrSettingsUiState = JellyseerrSettingsUiState(
      snackbarMessage = null,
      dialogState = null,
      seerrState = SeerrState.Login(
        isLoading = false,
      ),
    )
  }
}
