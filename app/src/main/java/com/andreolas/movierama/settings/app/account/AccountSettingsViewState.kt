package com.andreolas.movierama.settings.app.account

import com.andreolas.movierama.session.model.AccountDetails
import com.andreolas.movierama.ui.UIText

data class AccountSettingsViewState(
  val requestToken: String?,
  val navigateToWebView: Boolean?,
  val dialogMessage: UIText?, // TODO Create a dialog wrapper class
  val dialogTitle: UIText?,
  val accountDetails: AccountDetails?
) {
  companion object {
    fun initial(): AccountSettingsViewState {
      return AccountSettingsViewState(
        requestToken = null,
        navigateToWebView = null,
        dialogMessage = null,
        dialogTitle = null,
        accountDetails = null
      )
    }
  }
}
