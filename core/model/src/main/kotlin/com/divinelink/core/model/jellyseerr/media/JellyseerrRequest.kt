package com.divinelink.core.model.jellyseerr.media

data class JellyseerrRequest(
  val id: Int,
  val status: JellyseerrStatus.Request,
  val requester: JellyseerrRequester,
  val requestDate: String,
  val seasons: List<Int>,
)
