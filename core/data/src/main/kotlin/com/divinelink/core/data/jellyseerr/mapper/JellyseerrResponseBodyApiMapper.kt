package com.divinelink.core.data.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequestResponse
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse

fun JellyseerrRequestMediaResponse.map() = JellyseerrMediaRequestResponse(
  requestId = requestId,
  message = message,
  mediaInfo = JellyseerrMediaInfo(
    mediaId = media.tmdbId,
    seasons = seasons?.associate {
      it.seasonNumber to JellyseerrStatus.Media.from(it.status)
    } ?: emptyMap(),
    status = JellyseerrStatus.Media.from(status),
    requests = media.requests.map(),
  ),
)
