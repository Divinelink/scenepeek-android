package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param status The status of the request
 */
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
  /**
   * @param status The status of the media
   */
  @Serializable
  data class MediaResponse(
    val id: Int,
    val mediaType: String,
    val status: Int,
    val tmdbId: Int,
    val requests: List<MediaInfoRequestResponse>? = null,
  )
}
