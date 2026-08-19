package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.login.SeerrCredentials
import kotlinx.serialization.Serializable

sealed interface JellyseerrLoginRequestBodyApi {
  @Serializable
  data class Jellyfin(
    val username: String,
    val password: String,
    val serverType: Int,
  ) : JellyseerrLoginRequestBodyApi

  @Serializable
  data class Jellyseerr(
    val email: String,
    val password: String,
  ) : JellyseerrLoginRequestBodyApi

  @Serializable
  data class Plex(
    val authToken: String,
  ) : JellyseerrLoginRequestBodyApi
}

fun SeerrCredentials.toRequestBody(): JellyseerrLoginRequestBodyApi = when (this) {
  is SeerrCredentials.Plex -> JellyseerrLoginRequestBodyApi.Plex(
    authToken = authToken.normalized,
  )
  is SeerrCredentials.Seerr -> JellyseerrLoginRequestBodyApi.Jellyseerr(
    email = username.normalized,
    password = password.value,
  )
  is SeerrCredentials.StandardAuth.Jellyfin -> JellyseerrLoginRequestBodyApi.Jellyfin(
    username = username.normalized,
    password = password.value,
    serverType = serverType,
  )
  is SeerrCredentials.StandardAuth.Emby -> JellyseerrLoginRequestBodyApi.Jellyfin(
    username = username.normalized,
    password = password.value,
    serverType = serverType,
  )
}
