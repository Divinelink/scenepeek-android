package com.divinelink.core.fixtures.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse

object MediaInfoRequestResponseFactory {

  fun betterCallSaul1() = MediaInfoRequestResponse(
    seasons = listOf(
      TvSeasonResponse(
        seasonNumber = 2,
        status = JellyseerrStatus.Media.AVAILABLE.status,
      ),
      TvSeasonResponse(
        seasonNumber = 3,
        status = JellyseerrStatus.Media.AVAILABLE.status,
      ),
    ),
    status = JellyseerrStatus.Request.PENDING.status,
    createdAt = "2025-06-22T13:00:22.000Z",
    updatedAt = "2025-06-23T13:00:22.000Z",
    requestedBy = RequestedByResponseFactory.rhea(),
    id = 2,
  )

  fun betterCallSaul2() = MediaInfoRequestResponse(
    seasons = listOf(
      TvSeasonResponse(
        seasonNumber = 5,
        status = JellyseerrStatus.Media.PROCESSING.status,
      ),
    ),
    status = JellyseerrStatus.Request.APPROVED.status,
    createdAt = "2025-06-21T13:00:22.000Z",
    updatedAt = "2025-06-24T13:00:22.000Z",
    requestedBy = RequestedByResponseFactory.rhea(),
    id = 3,
  )

  fun betterCallSaul3() = MediaInfoRequestResponse(
    seasons = listOf(
      TvSeasonResponse(
        seasonNumber = 1,
        status = JellyseerrStatus.Media.PROCESSING.status,
      ),
      TvSeasonResponse(
        seasonNumber = 6,
        status = JellyseerrStatus.Media.PENDING.status,
      ),
    ),
    status = JellyseerrStatus.Request.DECLINED.status,
    createdAt = "2025-06-23T13:00:22.000Z",
    updatedAt = "2025-06-24T13:00:22.000Z",
    requestedBy = RequestedByResponseFactory.bob(),
    id = 4,
  )

  fun all() = listOf(
    betterCallSaul1(),
    betterCallSaul2(),
    betterCallSaul3(),
  )
}
