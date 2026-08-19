package com.divinelink.core.model.jellyseerr.login

import com.divinelink.core.model.Address
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username

data class SeerrLoginData(
  val address: Address,
  val credentials: SeerrCredentials,
) {
  companion object {
    val initial = SeerrLoginData(
      address = Address.empty,
      credentials = SeerrCredentials.StandardAuth.Jellyfin(
        username = Username.empty,
        password = Password.empty,
      ),
    )
  }
}
