package com.divinelink.core.network.jellyseerr.mapper.movie

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.movie.MovieInfoResponse

fun MovieInfoResponse.map() = JellyseerrMediaInfo(
  mediaId = id,
  status = JellyseerrStatus.Media.from(status),
  requests = requests.map(),
  seasons = emptyList(),
)
