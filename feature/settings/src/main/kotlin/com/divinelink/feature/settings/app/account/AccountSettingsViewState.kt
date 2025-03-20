package com.divinelink.feature.settings.app.account

import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.ui.components.dialog.AlertDialogUiState

data class AccountSettingsViewState(
  val alertDialogUiState: AlertDialogUiState?,
  val accountDetails: AccountDetails?,
  val jellyseerrAccountDetails: JellyseerrAccountDetails?,
) {
  companion object {
    fun initial(): AccountSettingsViewState = AccountSettingsViewState(
      alertDialogUiState = null,
      accountDetails = null,
      jellyseerrAccountDetails = null,
    )
  }
}
