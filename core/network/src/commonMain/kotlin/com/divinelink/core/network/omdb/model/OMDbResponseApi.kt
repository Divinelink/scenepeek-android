package com.divinelink.core.network.omdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OMDbResponseApi(
  @SerialName("Metascore") val metascore: String,
  val imdbRating: String,
  val imdbVotes: String,
  @SerialName("Ratings") val ratings: List<OMDbRatingSourceResponse>,
) {
  @Serializable
  data class OMDbRatingSourceResponse(
    @SerialName("Source") val source: String,
    @SerialName("Value") val value: String,
  )
}
