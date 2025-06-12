package com.divinelink.core.network.jellyseerr.model.tv

import kotlinx.serialization.Serializable

@Serializable
data class TvSeasonResponse(
  val seasonNumber: Int,
  val status: Int,
)
