package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.Password
import com.divinelink.core.model.Username

data class JellyseerrLoginData(
  val address: String,
  val username: Username,
  val password: Password,
  val authMethod: JellyseerrAuthMethod,
) {
  companion object {
    fun empty() = JellyseerrLoginData(
      address = "",
      username = Username.empty(),
      password = Password.empty(),
      authMethod = JellyseerrAuthMethod.JELLYFIN,
    )

    fun prefilled(address: String = "") = JellyseerrLoginData(
      address = address,
      username = Username.empty(),
      password = Password.empty(),
      authMethod = JellyseerrAuthMethod.JELLYFIN,
    )
  }
}
