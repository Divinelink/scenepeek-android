package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.Address
import com.divinelink.core.model.jellyseerr.login.SeerrLoginForm

sealed class SeerrState(
  open var loginForm: SeerrLoginForm = SeerrLoginForm(),
) {
  data class Login(
    val isLoading: Boolean,
    override var loginForm: SeerrLoginForm = SeerrLoginForm(),
  ) : SeerrState(
    loginForm = loginForm,
  ) {
    val isLoginEnabled = loginForm.isLoginEnabled
  }

  data class LoggedIn(
    val isLoading: Boolean,
    val accountDetails: JellyseerrProfile,
    val address: String,
  ) : SeerrState(
    loginForm = SeerrLoginForm(
      address = Address.from(address),
    ),
  )
}
