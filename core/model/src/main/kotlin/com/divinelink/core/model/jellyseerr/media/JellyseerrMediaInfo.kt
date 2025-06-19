package com.divinelink.core.model.jellyseerr.media

data class JellyseerrMediaInfo(
  val status: JellyseerrMediaStatus,
  val mediaId: Int,
  val requests: List<JellyseerrRequest>,
  val seasons: Map<Int, JellyseerrMediaStatus>,
) 
