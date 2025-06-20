package com.divinelink.core.model.jellyseerr.media

data class JellyseerrMediaInfo(
  val mediaId: Int,
  val status: JellyseerrMediaStatus,
  val requests: List<JellyseerrRequest>,
  val seasons: Map<Int, JellyseerrMediaStatus>,
)
