package com.divinelink.core.model.jellyseerr

sealed class JellyseerrState(
  open var address: String,
  open var jellyfinLogin: JellyfinLogin = JellyfinLogin.empty(),
  open var jellyseerrLogin: JellyseerrLogin = JellyseerrLogin.empty(),
) {
  data class Initial(
    val isLoading: Boolean,
    val preferredOption: JellyseerrLoginMethod? = null,
    override var address: String = "",
    override var jellyfinLogin: JellyfinLogin = JellyfinLogin.empty(),
    override var jellyseerrLogin: JellyseerrLogin = JellyseerrLogin.empty(),
  ) : JellyseerrState(
    address = address,
    jellyfinLogin = jellyfinLogin,
    jellyseerrLogin = jellyseerrLogin,
  ) {
    val isLoginEnabled = preferredOption != null
  }

  data class LoggedIn(
    val isLoading: Boolean,
    val loginData: JellyseerrAccountStatus,
  ) : JellyseerrState(address = loginData.address)
}
