package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrRequestMediaBodyApi(
  val mediaType: String,
  val mediaId: Int,
  val is4k: Boolean,
  val seasons: List<Int>,
)
