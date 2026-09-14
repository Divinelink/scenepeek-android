package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class WatchedEpisodeExport(
  val episodeId: Long,
  val watchedAt: String?,
)
