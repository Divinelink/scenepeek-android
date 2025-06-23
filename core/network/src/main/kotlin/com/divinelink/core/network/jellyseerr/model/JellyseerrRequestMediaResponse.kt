package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrRequestMediaResponse(
  @SerialName("id") val requestId: Int,
  val message: String? = null,
  val media: MediaResponse,
  val type: String? = null,
  val status: Int? = null,
  val seasons: List<TvSeasonResponse>? = null,
  val requestedBy: RequestedByResponse,
  val createdAt: String,
) {
  @Serializable
  data class MediaResponse(
    val tmdbId: Int,
    val requests: List<MediaInfoRequestResponse>? = null,
  )
}
