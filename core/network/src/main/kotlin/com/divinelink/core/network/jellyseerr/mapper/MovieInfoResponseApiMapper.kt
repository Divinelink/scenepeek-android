package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.model.movie.MovieInfoResponse

fun MovieInfoResponse.map() = JellyseerrMediaInfo.Movie(
  status = JellyseerrMediaStatus.from(status),
)
