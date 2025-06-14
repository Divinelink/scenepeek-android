package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse
import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrRequestMediaResponse(
  val message: String? = null,
  val type: String? = null,
  val status: Int? = null,
  val seasons: List<TvSeasonResponse>? = null,
)
