package com.divinelink.core.network.media.model.details.watchlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AddToWatchlistRequestApi(
  open val mediaId: Int,
  open val mediaType: String,
) {
  abstract val accountId: String
  abstract val sessionId: String
  abstract val addToWatchlist: Boolean

  @Serializable
  data class Movie(
    @SerialName("movie_id") val movieId: Int,
    override val accountId: String,
    override val sessionId: String,
    override val addToWatchlist: Boolean,
  ) : AddToWatchlistRequestApi(
    mediaId = movieId,
    mediaType = "movie",
  )

  @Serializable
  data class TV(
    @SerialName("series_id") val seriesId: Int,
    override val accountId: String,
    override val sessionId: String,
    override val addToWatchlist: Boolean,
  ) : AddToWatchlistRequestApi(
    mediaId = seriesId,
    mediaType = "tv",
  )
}
