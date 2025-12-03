package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
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
}

fun JellyseerrAuthMethod.toRequestBodyApi(
  username: String,
  password: String,
): JellyseerrLoginRequestBodyApi = when (this) {
  JellyseerrAuthMethod.JELLYFIN,
  JellyseerrAuthMethod.EMBY,
    -> JellyseerrLoginRequestBodyApi.Jellyfin(
      username = username,
      password = password,
      serverType = serverType,
    )
  JellyseerrAuthMethod.JELLYSEERR -> JellyseerrLoginRequestBodyApi.Jellyseerr(
    email = username,
    password = password,
  )
}
