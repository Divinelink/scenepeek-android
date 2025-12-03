package com.divinelink.core.network.media.model.details.season

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonResponseApi(
  val id: Int,
  val name: String,
  val overview: String,
  @SerialName("air_date") val airDate: String?,
  @SerialName("episode_count") val episodeCount: Int,
  @SerialName("poster_path") val posterPath: String?,
  @SerialName("season_number") val seasonNumber: Int,
  @SerialName("vote_average") val voteAverage: Double,
)
