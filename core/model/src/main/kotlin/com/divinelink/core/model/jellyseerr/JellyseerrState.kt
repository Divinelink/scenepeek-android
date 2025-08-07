package com.divinelink.core.model.jellyseerr

sealed class JellyseerrState(
  open var loginData: JellyseerrLoginData = JellyseerrLoginData.empty(),
) {
  data class Login(
    val isLoading: Boolean,
    override var loginData: JellyseerrLoginData = JellyseerrLoginData.empty(),
  ) : JellyseerrState(
    loginData = loginData,
  ) {
    val isLoginEnabled = loginData.address.isNotEmpty() &&
      loginData.username.value.isNotEmpty() &&
      loginData.password.value.isNotEmpty()
  }

  data class LoggedIn(
    val isLoading: Boolean,
    val accountDetails: JellyseerrAccountDetails,
  ) : JellyseerrState()
}
