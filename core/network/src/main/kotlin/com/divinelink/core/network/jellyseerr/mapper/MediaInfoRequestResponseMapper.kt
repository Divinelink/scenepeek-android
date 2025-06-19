package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.network.jellyseerr.model.movie.MediaInfoRequestResponse

fun List<MediaInfoRequestResponse>?.map() = this?.map { it.map() } ?: emptyList()

fun MediaInfoRequestResponse.map() = JellyseerrRequest.Movie(
  id = id,
  status = JellyseerrMediaStatus.from(status),
  requester = requestedBy.map(),
)
