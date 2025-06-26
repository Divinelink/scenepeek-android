package com.divinelink.core.network.jellyseerr.mapper.tv

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.network.jellyseerr.mapper.map
import com.divinelink.core.network.jellyseerr.model.tv.TvInfoResponse

fun TvInfoResponse.map() = JellyseerrMediaInfo(
  mediaId = id,
  status = JellyseerrStatus.Media.from(status),
  seasons = buildList {
    val seasonRequests = seasons.map {
      SeasonRequest(it.seasonNumber, JellyseerrStatus.Media.from(it.status))
    }

    val requestSeasons = requests
      .filter { it.status != JellyseerrStatus.Request.DECLINED.status }
      .flatMap { request ->
        request.seasons.map { season ->
          SeasonRequest(
            seasonNumber = season.seasonNumber,
            status = when (request.status) {
              JellyseerrStatus.Request.FAILED.status -> JellyseerrStatus.Request.FAILED
              else -> JellyseerrStatus.Season.from(season.status)
            },
          )
        }
      }

    // Create merged lookup map with priority rules
    (seasonRequests + requestSeasons)
      .groupBy { it.seasonNumber }
      .values
      .mapNotNull { seasons ->
        seasons.firstOrNull { it.status != JellyseerrStatus.Media.UNKNOWN }
          ?: seasons.firstOrNull()
      }
      .forEach { add(it) }
  },
  requests = requests.map(),
)
