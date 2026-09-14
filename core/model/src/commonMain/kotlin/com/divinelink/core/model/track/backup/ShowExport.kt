package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class ShowExport(
  val tmdbId: Long,
  val title: String,
  val posterPath: String?,
  val status: String,
  val watchedEpisodes: List<WatchedEpisodeExport>,
  val addedAt: String,
)
