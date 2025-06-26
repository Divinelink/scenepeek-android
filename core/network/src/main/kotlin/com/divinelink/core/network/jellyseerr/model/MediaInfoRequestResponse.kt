package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse
import kotlinx.serialization.Serializable

@Serializable
data class MediaInfoRequestResponse(
  val id: Int,
  val status: Int,
  val media: JellyseerrRequestMediaResponse.MediaResponse,
  val createdAt: String,
  val updatedAt: String,
  val seasons: List<TvSeasonResponse>,
  val requestedBy: RequestedByResponse,
)
