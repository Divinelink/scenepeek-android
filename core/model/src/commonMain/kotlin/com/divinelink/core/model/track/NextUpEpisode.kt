package com.divinelink.core.model.track

import com.divinelink.core.model.details.Episode
import kotlinx.serialization.Serializable

@Serializable
data class NextUpEpisode(
  val episode: Episode,
  val showTitle: String,
  val showPosterPath: String?,
  val watchedEpisodes: Int,
  val totalEpisodes: Int,
)
