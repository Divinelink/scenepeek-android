package com.divinelink.core.network.jellyseerr.model.server.radarr

import kotlinx.serialization.Serializable

@Serializable
data class RadarrInstanceResponse(
  val id: Int,
  val name: String,
  val is4k: Boolean,
  val isDefault: Boolean,
  val activeDirectory: String,
  val activeProfileId: Int,
)
