package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

/**
 * Represents the user's tracked shows.
 */
@Serializable
data class ShowExport(
  val id: Long,
  val title: String,
  val posterPath: String?,
  val status: String,
  val watched: List<UserEpisodeExport>,
  val addedAt: String,
  val episodes: Int,
  val seasons: Int,
  val genres: String,
)
