package com.andreolas.movierama.base.data.remote.movies.dto.account.states

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AccountMediaDetailsRequestApi(
  open val id: Int,
  open val endpoint: String,
) {
  abstract val sessionId: String

  @Serializable
  data class Movie(
    @SerialName("movie_id") val movieId: Int,
    override val sessionId: String
  ) : AccountMediaDetailsRequestApi(
    id = movieId,
    endpoint = "movie",
  )

  @Serializable
  data class TV(
    @SerialName("series_id") val seriesId: Int,
    override val sessionId: String
  ) : AccountMediaDetailsRequestApi(
    id = seriesId,
    endpoint = "tv"
  )
}
