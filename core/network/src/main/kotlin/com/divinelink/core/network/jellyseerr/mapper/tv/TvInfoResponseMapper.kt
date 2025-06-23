package com.divinelink.core.network.jellyseerr.mapper.tv

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.tv.TvInfoResponse

fun TvInfoResponse.map() = JellyseerrMediaInfo(
  mediaId = id,
  status = JellyseerrStatus.Media.from(status),
  seasons = requests
    .flatMap { it.seasons }
    .associate {
      it.seasonNumber to JellyseerrStatus.Media.from(it.status)
    } + seasons
    .filterNot { it.status == 1 }
    .associate {
      it.seasonNumber to JellyseerrStatus.Media.from(it.status)
    },
  requests = requests.map(),
)
