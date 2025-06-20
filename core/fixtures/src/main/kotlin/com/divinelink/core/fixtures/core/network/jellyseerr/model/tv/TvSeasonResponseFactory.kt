package com.divinelink.core.fixtures.core.network.jellyseerr.model.tv

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse

object TvSeasonResponseFactory {

  fun season1() = TvSeasonResponse(
    seasonNumber = 1,
    status = JellyseerrMediaStatus.AVAILABLE.status,
  )

  fun season2() = TvSeasonResponse(
    seasonNumber = 2,
    status = JellyseerrMediaStatus.PARTIALLY_AVAILABLE.status,
  )

  fun partially() = listOf(
    season1(),
    season2(),
  )
}
