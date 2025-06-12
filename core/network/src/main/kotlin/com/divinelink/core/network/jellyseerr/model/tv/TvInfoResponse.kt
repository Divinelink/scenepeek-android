package com.divinelink.core.network.jellyseerr.model.tv

import kotlinx.serialization.Serializable

@Serializable
data class TvInfoResponse(
  val status: Int,
  val seasons: List<TvSeasonResponse>,
)
