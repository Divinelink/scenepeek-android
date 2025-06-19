package com.divinelink.core.network.jellyseerr.model.tv

import com.divinelink.core.network.jellyseerr.model.movie.MediaInfoRequestResponse
import kotlinx.serialization.Serializable

@Serializable
data class TvInfoResponse(
  val id: Int,
  val status: Int,
  val seasons: List<TvSeasonResponse>,
  val requests: List<MediaInfoRequestResponse>,
)
