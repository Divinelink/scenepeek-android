package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.model.tv.TvInfoResponse

fun TvInfoResponse.map() = JellyseerrMediaInfo.TV(
  status = JellyseerrMediaStatus.from(status),
  seasons = requests.flatMap { it.seasons }.associate {
    it.seasonNumber to JellyseerrMediaStatus.from(it.status)
  } + seasons.associate {
    it.seasonNumber to JellyseerrMediaStatus.from(it.status)
  },
)
