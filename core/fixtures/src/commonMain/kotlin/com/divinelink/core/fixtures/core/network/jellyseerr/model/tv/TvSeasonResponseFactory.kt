package com.divinelink.core.fixtures.core.network.jellyseerr.model.tv

import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse

object TvSeasonResponseFactory {

  fun season1() = TvSeasonResponse(
    seasonNumber = 1,
    status = JellyseerrStatus.Media.AVAILABLE.status,
  )

  fun season2() = TvSeasonResponse(
    seasonNumber = 2,
    status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE.status,
  )

  fun partially() = listOf(
    season1(),
    season2(),
  )
}
