package com.divinelink.core.data.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse

fun JellyseerrRequestMediaResponse.map() = MediaRequestResult(
  requestId = requestId,
  message = message,
  mediaInfo = JellyseerrMediaInfo(
    mediaId = media.id,
    seasons = seasons?.map {
      SeasonRequest(
        seasonNumber = it.seasonNumber,
        status = JellyseerrStatus.Media.from(media.status),
      )
    } ?: emptyList(),
    status = JellyseerrStatus.Media.from(media.status),
    requests = media.requests.map(),
  ),
)
