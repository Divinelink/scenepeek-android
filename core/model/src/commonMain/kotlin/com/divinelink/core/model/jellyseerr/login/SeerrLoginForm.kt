package com.divinelink.core.model.jellyseerr.login

import com.divinelink.core.model.Address
import com.divinelink.core.model.AuthToken
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod

data class SeerrLoginForm(
  val method: SeerrAuthMethod = SeerrAuthMethod.JELLYFIN,
  val address: Address = Address.empty,
  val username: Username = Username.empty,
  val password: Password = Password.empty,
  val authToken: AuthToken = AuthToken.empty,
) {
  val credentials: SeerrCredentials
    get() = when (method) {
      SeerrAuthMethod.PLEX -> SeerrCredentials.Plex(authToken)
      SeerrAuthMethod.SEERR -> SeerrCredentials.Seerr(username, password)
      SeerrAuthMethod.JELLYFIN -> SeerrCredentials.StandardAuth.Jellyfin(username, password)
      SeerrAuthMethod.EMBY -> SeerrCredentials.StandardAuth.Emby(username, password)
    }

  val loginData: JellyseerrLoginData?
    get() = JellyseerrLoginData(address, credentials)
      .takeIf { !address.isBlank && credentials.isComplete }

  val isLoginEnabled: Boolean get() = loginData != null
}
