package com.divinelink.core.model.details

import com.divinelink.core.model.media.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class Episode(
  val id: Int,
  val name: String,
  val airDate: String?,
  val overview: String?,
  val runtime: String?,
  val number: Int,
  val seasonNumber: Int,
  val showId: Long,
  val stillPath: String?,
  val voteAverage: String?,
  val voteCount: Int?,
  val crew: List<MediaItem.Person>,
  val guestStars: List<MediaItem.Person>,
  val accountRating: Int?,
)
