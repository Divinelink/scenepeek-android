package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.commons.extensions.localizeIsoDate
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse

fun List<MediaInfoRequestResponse>?.map() = this?.map { it.map() } ?: emptyList()

fun MediaInfoRequestResponse.map() = JellyseerrRequest(
  id = id,
  mediaStatus = JellyseerrStatus.Media.from(media.status),
  requestStatus = JellyseerrStatus.Request.from(status),
  requester = requestedBy.map(),
  seasons = seasons.map { it.seasonNumber },
  requestDate = createdAt.localizeIsoDate(),
)
