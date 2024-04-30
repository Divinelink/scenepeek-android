package com.andreolas.movierama.settings.app.account

data class AccountSettingsViewState(
  val requestToken: String?,
  val navigateToWebView: Boolean? = false,
  val toastText: String? = null
)
