package com.divinelink.core.model.session

import kotlinx.serialization.Serializable

@Serializable
data class TmdbSession(
  val sessionId: String,
  val accessToken: AccessToken,
)
