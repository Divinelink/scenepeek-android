package com.divinelink.core.network.media.model.rating

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AddRatingRequestApi(
  open val id: Int,
  open val endpoint: String,
) {
  abstract val sessionId: String
  abstract val rating: Int

  @Serializable
  data class Movie(
    @SerialName("movie_id") val movieId: Int,
    override val sessionId: String,
    override val rating: Int
  ) : AddRatingRequestApi(
    id = movieId,
    endpoint = "movie",
  )

  @Serializable
  data class TV(
    @SerialName("series_id") val seriesId: Int,
    override val sessionId: String,
    override val rating: Int
  ) : AddRatingRequestApi(
    id = seriesId,
    endpoint = "tv"
  )
}
