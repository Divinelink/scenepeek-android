package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.SeerrAuthMethod
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

fun SeerrAuthMethod.toRequestBodyApi(
  username: String,
  password: String,
): JellyseerrLoginRequestBodyApi = when (this) {
  SeerrAuthMethod.JELLYFIN,
  SeerrAuthMethod.EMBY,
    -> JellyseerrLoginRequestBodyApi.Jellyfin(
      username = username,
      password = password,
      serverType = serverType,
    )
  SeerrAuthMethod.SEERR -> JellyseerrLoginRequestBodyApi.Jellyseerr(
    email = username,
    password = password,
  )
}
