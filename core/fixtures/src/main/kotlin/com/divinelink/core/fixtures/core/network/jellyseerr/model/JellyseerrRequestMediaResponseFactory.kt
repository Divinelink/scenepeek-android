package com.divinelink.core.fixtures.core.network.jellyseerr.model

import com.divinelink.core.fixtures.core.network.jellyseerr.model.tv.TvSeasonResponseFactory
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse

object JellyseerrRequestMediaResponseFactory {

  fun movie(status: JellyseerrStatus.Media = JellyseerrStatus.Media.AVAILABLE) =
    JellyseerrRequestMediaResponse(
      message = null,
      type = "movie",
      status = status.status,
      media = JellyseerrRequestMediaResponse.MediaResponse(
        tmdbId = 496244,
        requests = null,
      ),
      seasons = emptyList(),
      requestedBy = RequestedByResponseFactory.bob(),
      requestId = 12,
      createdAt = "2025-06-22T12:07:59.000Z",
    )

  fun tv() = JellyseerrRequestMediaResponse(
    message = null,
    type = "tv",
    status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE.status,
    media = JellyseerrRequestMediaResponse.MediaResponse(
      tmdbId = 1399,
      requests = MediaInfoRequestResponseFactory.all(),
    ),
    seasons = TvSeasonResponseFactory.partially(),
    requestedBy = RequestedByResponseFactory.rhea(),
    requestId = 34,
    createdAt = "2025-06-22T12:07:59.000Z",
  )
}
