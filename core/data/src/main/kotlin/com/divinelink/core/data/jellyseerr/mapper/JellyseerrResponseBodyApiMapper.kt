package com.divinelink.core.data.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequestResponse
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse

fun JellyseerrRequestMediaResponse.map() = JellyseerrMediaRequestResponse(
  message = message,
  mediaInfo = if (MediaType.from(type) == MediaType.MOVIE) {
    JellyseerrMediaInfo.Movie(
      status = JellyseerrMediaStatus.from(status),
    )
  } else {
    JellyseerrMediaInfo.TV(
      seasons = seasons?.associate {
        it.seasonNumber to JellyseerrMediaStatus.from(it.status)
      } ?: emptyMap(),
      status = JellyseerrMediaStatus.from(status),
    )
  },
)
