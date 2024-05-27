package com.divinelink.core.network.media.model.details.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ReviewsRequestApi(
  open val id: Int,
  open val endpoint: String,
) {

  @Serializable
  data class Movie(
    @SerialName("movie_id")
    val movieId: Int,
  ) : ReviewsRequestApi(
    id = movieId,
    endpoint = "movie"
  )

  @Serializable
  data class TV(
    @SerialName("series_id")
    val seriesId: Int,
  ) : ReviewsRequestApi(
    id = seriesId,
    endpoint = "tv"
  )
}
