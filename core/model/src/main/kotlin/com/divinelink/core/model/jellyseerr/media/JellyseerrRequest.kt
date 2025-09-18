package com.divinelink.core.model.jellyseerr.media

import com.divinelink.core.model.media.MediaReference

data class JellyseerrRequest(
  val id: Int,
  val media: MediaReference,
  val mediaStatus: JellyseerrStatus.Media,
  val requestStatus: JellyseerrStatus.Request,
  val requester: JellyseerrRequester,
  val requestDate: String,
  val seasons: List<SeasonRequest>,
  val profileName: String?,
  val canRemove: Boolean,
)

data class SeasonRequest(
  val seasonNumber: Int,
  val status: JellyseerrStatus,
)
