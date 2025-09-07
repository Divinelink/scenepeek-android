package com.divinelink.core.data.jellyseerr.model

data class JellyseerrRequestParams(
  val mediaType: String,
  val mediaId: Int,
  val is4k: Boolean = false,
  val seasons: List<Int>,
  val serverId: Int?,
  val profileId: Int?,
  val rootFolder: String?,
)
