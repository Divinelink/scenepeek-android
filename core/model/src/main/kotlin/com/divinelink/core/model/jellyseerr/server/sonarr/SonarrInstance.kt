package com.divinelink.core.model.jellyseerr.server.sonarr

import kotlinx.serialization.Serializable

@Serializable
data class SonarrInstance(
  val id: Int,
  val name: String,
  val is4k: Boolean,
  val isDefault: Boolean,
  val activeDirectory: String,
  val activeProfileId: Int,
)
