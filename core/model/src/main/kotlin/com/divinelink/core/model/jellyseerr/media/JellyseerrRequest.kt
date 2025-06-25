package com.divinelink.core.model.jellyseerr.media

data class JellyseerrRequest(
  val id: Int,
  val mediaStatus: JellyseerrStatus.Media,
  val requestStatus: JellyseerrStatus.Request,
  val requester: JellyseerrRequester,
  val requestDate: String,
  val seasons: List<Int>,
)
