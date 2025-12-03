package com.divinelink.core.model.jellyseerr.media

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrRequester(
  val id: Int,
  val displayName: String,
)
