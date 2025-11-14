package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrEditRequestMediaBodyApi(
  val requestId: Int?,
  val mediaType: String,
  val mediaId: Int,
  val is4k: Boolean,
  val seasons: List<Int>,
  val serverId: Int? = null,
  val profileId: Int? = null,
  val rootFolder: String? = null,
)
