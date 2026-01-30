package com.divinelink.core.network.media.model.details.season

import com.divinelink.core.network.media.model.details.credits.CastApi
import com.divinelink.core.network.media.model.details.credits.CrewApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeResponse(
  val id: Int,
  @SerialName("air_date") val airDate: String?,
  @SerialName("episode_type") val episodeType: String,
  val name: String,
  val overview: String,
  val runtime: Int?,
  @SerialName("episode_number") val episodeNumber: Int,
  @SerialName("season_number") val seasonNumber: String,
  @SerialName("show_id") val showId: Int,
  @SerialName("still_path") val stillPath: String?,
  @SerialName("vote_average") val voteAverage: String?,
  @SerialName("vote_count") val voteCount: String,
  val crew: List<CrewApi>,
  @SerialName("guest_stars") val guestStars: List<CastApi>,
)
