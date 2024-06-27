package com.divinelink.feature.settings.app.account

import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.ui.snackbar.SnackbarMessage

data class AccountSettingsViewState(
  val requestToken: String?,
  val navigateToWebView: Boolean?,
  val alertDialogUiState: AlertDialogUiState?,
  val accountDetails: AccountDetails?,
  val snackbarMessage: SnackbarMessage?,
  val jellyseerrState: JellyseerrState,
) {
  companion object {
    fun initial(): AccountSettingsViewState = AccountSettingsViewState(
      requestToken = null,
      navigateToWebView = null,
      alertDialogUiState = null,
      accountDetails = null,
      snackbarMessage = null,
      jellyseerrState = JellyseerrState.Initial(isLoading = false, preferredOption = null),
    )
  }
}
