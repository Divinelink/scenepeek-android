package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.Password
import com.divinelink.core.model.Username

data class JellyseerrLoginParams(
  val username: Username,
  val password: Password,
  val address: String,
  val authMethod: JellyseerrAuthMethod,
) {
  fun toLoginData() = JellyseerrLoginData(
    address = address,
    username = username,
    password = password,
    serverType = authMethod.serverType,
  )
}
