package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class ShowExport(
  val id: Long,
  val title: String,
  val posterPath: String?,
  val status: String,
  val watched: List<UserEpisodeExport>,
  val addedAt: String,
)
