package com.divinelink.core.network.jellyseerr.model

import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse
import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrResponseBodyApi(
  val message: String? = null,
  val type: String? = null,
  val status: Int,
  val seasons: List<TvSeasonResponse>? = null,
)
