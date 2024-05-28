package com.divinelink.core.network.media.model.details.watchlist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddToWatchlistResponseApi(
  val success: Boolean,
  @SerialName("status_code") val statusCode: Int,
  @SerialName("status_message") val statusMessage: String,
)
