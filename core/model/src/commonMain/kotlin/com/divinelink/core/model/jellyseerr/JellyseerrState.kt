package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.Address
import com.divinelink.core.model.jellyseerr.login.SeerrLoginForm

sealed class JellyseerrState(
  open var loginForm: SeerrLoginForm = SeerrLoginForm(),
) {
  data class Login(
    val isLoading: Boolean,
    override var loginForm: SeerrLoginForm = SeerrLoginForm(),
  ) : JellyseerrState(
    loginForm = loginForm,
  ) {
    val isLoginEnabled = loginForm.isLoginEnabled
  }

  data class LoggedIn(
    val isLoading: Boolean,
    val accountDetails: JellyseerrProfile,
    val address: String,
  ) : JellyseerrState(
    loginForm = SeerrLoginForm(
      address = Address.from(address),
    ),
  )
}
