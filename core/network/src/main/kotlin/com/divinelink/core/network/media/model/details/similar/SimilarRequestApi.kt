package com.divinelink.core.network.media.model.details.similar

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SimilarRequestApi(
  val page: Int = 1,
  open val id: Int,
  open val endpoint: String,
) {

  @Serializable
  data class Movie(
    @SerialName("movie_id")
    val movieId: Int,
  ) : SimilarRequestApi(
    id = movieId,
    endpoint = "movie",
  )

  @Serializable
  data class TV(
    @SerialName("series_id")
    val seriesId: Int,
  ) : SimilarRequestApi(
    id = seriesId,
    endpoint = "tv",
  )
}
