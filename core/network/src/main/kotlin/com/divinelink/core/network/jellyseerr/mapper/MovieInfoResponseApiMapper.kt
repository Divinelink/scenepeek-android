package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.model.MovieInfoResponseApi

fun MovieInfoResponseApi.map() = JellyseerrMediaInfo.Movie(
  status = JellyseerrMediaStatus.from(status),
)
