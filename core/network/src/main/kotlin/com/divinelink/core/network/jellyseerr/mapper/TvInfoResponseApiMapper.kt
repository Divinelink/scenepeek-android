package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.model.TvInfoResponseApi

fun TvInfoResponseApi.map() = JellyseerrMediaInfo.TV(
  status = JellyseerrMediaStatus.from(status),
  seasons = seasons.map { it.map() },
)
