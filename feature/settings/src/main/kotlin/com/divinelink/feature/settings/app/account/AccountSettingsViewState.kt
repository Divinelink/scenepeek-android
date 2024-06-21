package com.divinelink.feature.settings.app.account

import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.ui.components.dialog.AlertDialogUiState

data class AccountSettingsViewState(
  val requestToken: String?,
  val navigateToWebView: Boolean?,
  val alertDialogUiState: AlertDialogUiState?,
  val accountDetails: AccountDetails?,
) {
  companion object {
    fun initial(): AccountSettingsViewState = AccountSettingsViewState(
      requestToken = null,
      navigateToWebView = null,
      alertDialogUiState = null,
      accountDetails = null,
    )
  }
}
