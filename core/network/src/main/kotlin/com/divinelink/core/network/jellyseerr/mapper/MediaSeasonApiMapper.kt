package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.model.jellyseerr.media.JellyseerrSeasonInfo
import com.divinelink.core.network.jellyseerr.model.TvSeasonApi

fun TvSeasonApi.map() = JellyseerrSeasonInfo(
  seasonNumber = seasonNumber,
  status = JellyseerrMediaStatus.from(status),
)
