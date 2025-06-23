package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.commons.extensions.localizeIsoDate
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.model.movie.MediaInfoRequestResponse

fun List<MediaInfoRequestResponse>?.map() = this?.map { it.map() } ?: emptyList()

fun MediaInfoRequestResponse.map() = JellyseerrRequest(
  id = id,
  status = JellyseerrStatus.Request.from(status),
  requester = requestedBy.map(),
  seasons = seasons.map { it.seasonNumber },
  requestDate = createdAt.localizeIsoDate(),
)
