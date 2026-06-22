package com.divinelink.core.model.details

import com.divinelink.core.model.media.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDetails(
  val id: Int,
  val name: String,
  val overview: String,
  val posterPath: String?,
  val airDate: String?,
  val episodeCount: Int,
  val voteAverage: Double,
  val episodes: List<Episode>,
  val totalRuntime: String?,
  val guestStars: List<MediaItem.Person>,
)
