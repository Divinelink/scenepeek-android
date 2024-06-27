package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.Password
import com.divinelink.core.model.Username

data class JellyseerrLoginData(
  val address: String,
  val username: Username,
  val password: Password,
) {
  companion object {
    fun empty() = JellyseerrLoginData("", Username.empty(), Password.empty())
  }
}
