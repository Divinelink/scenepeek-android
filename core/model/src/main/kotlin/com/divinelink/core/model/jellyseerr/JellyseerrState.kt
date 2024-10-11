package com.divinelink.core.model.jellyseerr

sealed class JellyseerrState(
  open var jellyfinLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
  open var jellyseerrLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
) {
  data object Loading : JellyseerrState()

  data class Initial(
    val address: String,
    val isLoading: Boolean,
    val preferredOption: JellyseerrAuthMethod? = null,
    override var jellyfinLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
    override var jellyseerrLogin: JellyseerrLoginData = JellyseerrLoginData.empty(),
  ) : JellyseerrState(
    jellyfinLogin = jellyfinLogin,
    jellyseerrLogin = jellyseerrLogin,
  ) {
    val isLoginEnabled = preferredOption != null
  }

  data class LoggedIn(
    val isLoading: Boolean,
    val accountDetails: JellyseerrAccountDetails,
  ) : JellyseerrState()
}

val JellyseerrState.loginParams: JellyseerrLoginParams?
  get() = when (this) {
    is JellyseerrState.Initial -> JellyseerrLoginParams(
      address = address,
      authMethod = preferredOption!!,
      username = when (preferredOption) {
        JellyseerrAuthMethod.JELLYFIN -> jellyfinLogin.username
        JellyseerrAuthMethod.JELLYSEERR -> jellyseerrLogin.username
      },
      password = when (preferredOption) {
        JellyseerrAuthMethod.JELLYFIN -> jellyfinLogin.password
        JellyseerrAuthMethod.JELLYSEERR -> jellyseerrLogin.password
      },
    )
    else -> null
  }
