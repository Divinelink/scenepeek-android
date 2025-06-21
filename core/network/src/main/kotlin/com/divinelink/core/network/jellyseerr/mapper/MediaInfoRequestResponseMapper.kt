package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.commons.Constants
import com.divinelink.core.commons.extensions.formatTo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.network.jellyseerr.model.movie.MediaInfoRequestResponse

fun List<MediaInfoRequestResponse>?.map(isTv: Boolean) = this?.map { it.map(isTv) } ?: emptyList()

fun MediaInfoRequestResponse.map(isTv: Boolean) = if (isTv) { // TODO is this needed?
  JellyseerrRequest.TV(
    id = id,
    status = JellyseerrMediaStatus.from(status),
    requester = requestedBy.map(),
    seasons = seasons.map { it.seasonNumber },
    requestDate = createdAt.formatTo(
      inputFormat = Constants.ISO_8601,
      outputFormat = Constants.MMMM_DD_YYYY,
    ) ?: createdAt,
  )
} else {
  JellyseerrRequest.Movie(
    id = id,
    status = JellyseerrMediaStatus.from(status),
    requester = requestedBy.map(),
    requestDate = createdAt.formatTo(
      inputFormat = Constants.ISO_8601,
      outputFormat = Constants.MMMM_DD_YYYY,
    ) ?: createdAt,
  )
}
