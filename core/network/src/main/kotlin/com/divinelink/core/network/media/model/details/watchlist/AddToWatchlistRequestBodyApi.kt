package com.divinelink.core.network.media.model.details.watchlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToWatchlistRequestBodyApi(
  @SerialName("media_type") val mediaType: String,
  @SerialName("media_id") val mediaId: Int,
  val watchlist: Boolean,
)
