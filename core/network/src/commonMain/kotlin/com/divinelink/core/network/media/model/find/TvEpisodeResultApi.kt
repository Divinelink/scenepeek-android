package com.divinelink.core.network.media.model.find

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvEpisodeResultApi(
  val id: Int,
  val name: String,
  val overview: String,
  @SerialName("media_type") val mediaType: String,
  @SerialName("vote_average") val voteAverage: Double,
  @SerialName("vote_count") val voteCount: Int,
  @SerialName("air_date") val airDate: String,
  @SerialName("episode_number") val episodeNumber: Int,
  @SerialName("episode_type") val episodeType: String,
  @SerialName("production_code") val productionCode: String,
  val runtime: Int,
  @SerialName("season_number") val seasonNumber: Int,
  @SerialName("show_id") val showId: Int,
  @SerialName("still_path") val stillPath: String?,
)
