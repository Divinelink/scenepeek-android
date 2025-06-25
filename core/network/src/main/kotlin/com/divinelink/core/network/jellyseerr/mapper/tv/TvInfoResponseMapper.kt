package com.divinelink.core.network.jellyseerr.mapper.tv

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.tv.TvInfoResponse
import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse

fun TvInfoResponse.map() = JellyseerrMediaInfo(
  mediaId = id,
  status = JellyseerrStatus.Media.from(status),
  seasons = requests
    .flatMap { request ->
      request.seasons.map { season ->
        TvSeasonResponse(
          seasonNumber = season.seasonNumber,
          status = request.media.status,
        )
      }
    }
    .associate {
      it.seasonNumber to JellyseerrStatus.Media.from(it.status)
    } + seasons
    .filterNot { it.status == 1 }
    .associate {
      it.seasonNumber to JellyseerrStatus.Media.from(it.status)
    },
  requests = requests.map(),
)
