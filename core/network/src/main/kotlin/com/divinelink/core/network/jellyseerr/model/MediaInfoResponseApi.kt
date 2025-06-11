package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieInfoResponseApi(val status: Int)

@Serializable
data class TvInfoResponseApi(
  val status: Int,
  val seasons: List<TvSeasonApi>,
)

@Serializable
data class TvSeasonApi(
  val seasonNumber: Int,
  val status: Int,
)
