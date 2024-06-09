package com.andreolas.movierama.settings.app.account

import com.andreolas.movierama.ui.components.dialog.AlertDialogUiState
import com.divinelink.core.model.account.AccountDetails

data class AccountSettingsViewState(
  val requestToken: String?,
  val navigateToWebView: Boolean?,
  val alertDialogUiState: AlertDialogUiState?,
  val accountDetails: AccountDetails?
) {
  companion object {
    fun initial(): AccountSettingsViewState {
      return AccountSettingsViewState(
        requestToken = null,
        navigateToWebView = null,
        alertDialogUiState = null,
        accountDetails = null
      )
    }
  }
}
