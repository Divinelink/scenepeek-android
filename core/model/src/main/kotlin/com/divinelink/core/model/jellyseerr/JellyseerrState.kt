package com.divinelink.core.model.jellyseerr

sealed class JellyseerrState(
  open var address: String,
  open var jellyfinLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
  open var jellyseerrLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
) {
  data class Initial(
    val isLoading: Boolean,
    val preferredOption: JellyseerrLoginMethod? = null,
    override var address: String = "",
    override var jellyfinLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
    override var jellyseerrLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
  ) : JellyseerrState(
    address = address,
    jellyfinLogin = jellyfinLogin,
    jellyseerrLogin = jellyseerrLogin,
  ) {
    val isLoginEnabled = preferredOption != null
  }

  data class LoggedIn(
    val isLoading: Boolean,
    val accountDetails: JellyseerrAccountDetails,
  ) : JellyseerrState(address = "")
}

val JellyseerrState.loginParams: JellyseerrLoginParams?
  get() = when (this) {
    is JellyseerrState.Initial -> JellyseerrLoginParams(
      address = address,
      signInMethod = preferredOption!!,
      username = when (preferredOption) {
        JellyseerrLoginMethod.JELLYFIN -> jellyfinLogin.username
        JellyseerrLoginMethod.JELLYSEERR -> jellyseerrLogin.username
      },
      password = when (preferredOption) {
        JellyseerrLoginMethod.JELLYFIN -> jellyfinLogin.password
        JellyseerrLoginMethod.JELLYSEERR -> jellyseerrLogin.password
      },
    )
    else -> null
  }
