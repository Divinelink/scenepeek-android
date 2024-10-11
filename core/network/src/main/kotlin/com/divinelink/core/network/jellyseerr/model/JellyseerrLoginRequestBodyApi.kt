package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import kotlinx.serialization.Serializable

sealed interface JellyseerrLoginRequestBodyApi {
  @Serializable
  data class Jellyfin(
    val username: String,
    val password: String,
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
  JellyseerrAuthMethod.JELLYFIN -> JellyseerrLoginRequestBodyApi.Jellyfin(
    username = username,
    password = password,
  )
  JellyseerrAuthMethod.JELLYSEERR -> JellyseerrLoginRequestBodyApi.Jellyseerr(
    email = username,
    password = password,
  )
}
