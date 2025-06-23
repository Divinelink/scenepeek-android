package com.divinelink.core.network.jellyseerr.model.movie

import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import kotlinx.serialization.Serializable

@Serializable
data class MovieInfoResponse(
  val id: Int,
  val status: Int,
  val requests: List<MediaInfoRequestResponse>,
)
