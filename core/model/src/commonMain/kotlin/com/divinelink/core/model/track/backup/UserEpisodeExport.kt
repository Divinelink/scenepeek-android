package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class UserEpisodeExport(
  val id: Long,
  val watchedAt: String?,
)
