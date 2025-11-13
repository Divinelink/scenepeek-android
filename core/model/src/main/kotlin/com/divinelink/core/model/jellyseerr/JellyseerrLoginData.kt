package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username

data class JellyseerrLoginData(
  val address: Address,
  val username: Username,
  val password: Password,
  val authMethod: JellyseerrAuthMethod,
) {
  companion object {
    fun empty() = JellyseerrLoginData(
      address = Address.empty(),
      username = Username.empty(),
      password = Password.empty(),
      authMethod = JellyseerrAuthMethod.JELLYFIN,
    )

    fun prefilled(address: String = "") = JellyseerrLoginData(
      address = Address.from(address),
      username = Username.empty(),
      password = Password.empty(),
      authMethod = JellyseerrAuthMethod.JELLYFIN,
    )
  }
}
