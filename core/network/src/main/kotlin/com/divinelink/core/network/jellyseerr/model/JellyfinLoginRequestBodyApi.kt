package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyfinLoginRequestBodyApi(
  val username: String,
  val password: String,
)
