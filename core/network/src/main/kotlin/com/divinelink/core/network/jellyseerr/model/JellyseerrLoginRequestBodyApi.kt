package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrLoginRequestBodyApi(
  val email: String,
  val password: String,
)
