package com.divinelink.core.network.media.model.details.season

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonDetailsResponse(
  val id: Int,
  val name: String,
  val overview: String,
  @SerialName("air_date") val airDate: String?,
  @SerialName("poster_path") val posterPath: String?,
  @SerialName("vote_average") val voteAverage: Double,
  val episodes: List<EpisodeResponse>,
)
