package com.divinelink.core.model.jellyseerr.login

import com.divinelink.core.model.AuthToken
import com.divinelink.core.model.Password
import com.divinelink.core.model.Username
import com.divinelink.core.model.jellyseerr.SeerrAuthMethod

sealed interface SeerrCredentials {
  val method: SeerrAuthMethod
  val endpoint: String
  val serverType: Int?
  val isComplete: Boolean

  data class Plex(val authToken: AuthToken) : SeerrCredentials {
    override val method get() = SeerrAuthMethod.PLEX
    override val endpoint get() = "plex"
    override val serverType: Int? get() = null
    override val isComplete get() = !authToken.isBlank
  }

  data class Seerr(
    override val username: Username,
    override val password: Password,
  ) : HasUsername {
    override val method get() = SeerrAuthMethod.SEERR
    override val endpoint get() = "local"
    override val serverType: Int? get() = null
    override val isComplete get() = !username.isBlank && !password.isBlank
  }

  sealed interface StandardAuth : HasUsername {
    override val isComplete get() = !username.isBlank

    data class Jellyfin(
      override val username: Username,
      override val password: Password,
    ) : StandardAuth {
      override val method get() = SeerrAuthMethod.JELLYFIN
      override val endpoint get() = "jellyfin"
      override val serverType get() = 3
    }

    data class Emby(
      override val username: Username,
      override val password: Password,
    ) : StandardAuth {
      override val method get() = SeerrAuthMethod.EMBY
      override val endpoint get() = "jellyfin"
      override val serverType get() = 4
    }
  }
}

sealed interface HasUsername : SeerrCredentials {
  val username: Username
  val password: Password
}
