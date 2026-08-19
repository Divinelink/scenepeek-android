package com.divinelink.core.model.jellyseerr.login

import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username

data class JellyseerrLoginData(
  val address: Address,
  val credentials: SeerrCredentials,
) {
  companion object {
    val initial = JellyseerrLoginData(
      address = Address.empty,
      credentials = SeerrCredentials.StandardAuth.Jellyfin(
        username = Username.empty,
        password = Password.empty,
      ),
    )
  }
}
