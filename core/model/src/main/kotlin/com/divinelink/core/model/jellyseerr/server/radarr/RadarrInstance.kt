package com.divinelink.core.model.jellyseerr.server.radarr

import kotlinx.serialization.Serializable

@Serializable
data class RadarrInstance(
  val id: Int,
  val name: String,
  val is4k: Boolean,
  val isDefault: Boolean,
  val activeDirectory: String,
  val activeProfileId: Int,
)
