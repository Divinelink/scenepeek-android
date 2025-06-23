package com.divinelink.core.model.jellyseerr.media

data class JellyseerrMediaInfo(
  val mediaId: Int,
  val status: JellyseerrStatus.Media,
  val requests: List<JellyseerrRequest>,
  val seasons: Map<Int, JellyseerrStatus.Media>,
)
