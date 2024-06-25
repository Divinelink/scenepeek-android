package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.Password
import com.divinelink.core.model.Username

sealed class JellyseerrState(
  open var address: String,
  open var jellyfinLogin: JellyfinLogin = JellyfinLogin(Username.empty(), Password.empty()),
  open var jellyseerrLogin: JellyseerrLogin = JellyseerrLogin(Username.empty(), Password.empty()),
) {
  data class Initial(
    val preferredOption: JellyseerrLoginMethod?,
    override var address: String = "",
    override var jellyfinLogin: JellyfinLogin = JellyfinLogin(Username.empty(), Password.empty()),
    override var jellyseerrLogin: JellyseerrLogin = JellyseerrLogin(
      Username.empty(),
      Password.empty(),
    ),
  ) : JellyseerrState(
    address = address,
    jellyfinLogin = jellyfinLogin,
    jellyseerrLogin = jellyseerrLogin,
  ) {
    val isLoginEnabled = preferredOption != null
  }

  data class LoggedIn(
    override var address: String,
    val loginMethod: JellyseerrLoginMethod,
  ) : JellyseerrState(address)
}
