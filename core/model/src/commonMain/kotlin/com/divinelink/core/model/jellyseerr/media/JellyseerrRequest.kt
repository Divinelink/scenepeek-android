package com.divinelink.core.model.jellyseerr.media

import com.divinelink.core.model.media.MediaReference
import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrRequest(
  val id: Int,
  val jellyseerrMediaId: Int,
  val media: MediaReference,
  val mediaStatus: JellyseerrStatus.Media,
  val requestStatus: JellyseerrStatus.Request,
  val requester: JellyseerrRequester,
  val requestDate: String,
  val seasons: List<SeasonRequest>,
  val profileName: String?,
  val canRemove: Boolean,
  val profileId: Int? = null,
  val serverId: Int? = null,
  val rootFolder: String? = null,
) {
  val status: JellyseerrStatus
    get() = when (requestStatus) {
      JellyseerrStatus.Request.DECLINED -> requestStatus
      JellyseerrStatus.Request.FAILED -> requestStatus
      else -> mediaStatus
    }
}

@Serializable
data class SeasonRequest(
  val seasonNumber: Int,
  val status: JellyseerrStatus,
)
