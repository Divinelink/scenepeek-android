package com.divinelink.core.model.track

import com.divinelink.core.model.details.Episode
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class WatchedEpisode(
  val episode: Episode,
  val watchedId: Long,
  val showTitle: String,
  val showPosterPath: String?,
  val watchedAt: LocalDate,
)
