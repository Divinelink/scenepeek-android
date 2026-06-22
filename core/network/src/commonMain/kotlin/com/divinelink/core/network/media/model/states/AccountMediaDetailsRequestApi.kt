package com.divinelink.core.network.media.model.states

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AccountMediaDetailsRequestApi(
  open val id: Long,
  open val endpoint: String,
) {
  abstract val sessionId: String

  @Serializable
  data class Movie(
    @SerialName("movie_id") val movieId: Long,
    override val sessionId: String,
  ) : AccountMediaDetailsRequestApi(
    id = movieId,
    endpoint = "movie",
  )

  @Serializable
  data class TV(
    @SerialName("series_id") val seriesId: Long,
    override val sessionId: String,
  ) : AccountMediaDetailsRequestApi(
    id = seriesId,
    endpoint = "tv",
  )
}
