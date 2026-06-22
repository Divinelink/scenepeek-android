package com.divinelink.core.network.media.model.details.watchlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AddToAccountRequest(
  open val mediaId: Long,
  open val mediaType: String,
) {
  abstract val accountId: String
  abstract val sessionId: String
  abstract val watchlist: Boolean?
  abstract val favorite: Boolean?

  @Serializable
  data class Movie(
    @SerialName("movie_id") val movieId: Long,
    override val accountId: String,
    override val sessionId: String,
    override val watchlist: Boolean?,
    override val favorite: Boolean?,
  ) : AddToAccountRequest(
    mediaId = movieId,
    mediaType = "movie",
  )

  @Serializable
  data class TV(
    @SerialName("series_id") val seriesId: Long,
    override val accountId: String,
    override val sessionId: String,
    override val watchlist: Boolean?,
    override val favorite: Boolean?,
  ) : AddToAccountRequest(
    mediaId = seriesId,
    mediaType = "tv",
  )
}
