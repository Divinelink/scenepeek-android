package com.divinelink.core.model.jellyseerr.media

data class JellyseerrMediaInfo(
  val mediaId: Long,
  val status: JellyseerrStatus.Media,
  val requests: List<JellyseerrRequest>,
  val seasons: List<SeasonRequest>,
)
