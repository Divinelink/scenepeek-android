package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
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

fun JellyseerrLoginMethod.toRequestBodyApi(
  username: String,
  password: String,
): JellyseerrLoginRequestBodyApi = when (this) {
  JellyseerrLoginMethod.JELLYFIN -> JellyseerrLoginRequestBodyApi.Jellyfin(
    username = username,
    password = password,
  )
  JellyseerrLoginMethod.JELLYSEERR -> JellyseerrLoginRequestBodyApi.Jellyseerr(
    email = username,
    password = password,
  )
}
