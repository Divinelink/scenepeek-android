package com.divinelink.core.network.jellyseerr.mapper.tv

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.tv.TvInfoResponse

fun TvInfoResponse.map() = JellyseerrMediaInfo(
  mediaId = id,
  status = JellyseerrMediaStatus.from(status),
  seasons = requests
    .flatMap { it.seasons }
    .associate {
      it.seasonNumber to JellyseerrMediaStatus.from(it.status)
    } + seasons
    .filterNot { it.status == 1 }
    .associate {
      it.seasonNumber to JellyseerrMediaStatus.from(it.status)
    },
  requests = requests.map { it.map() },
)
