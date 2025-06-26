package com.divinelink.core.fixtures.core.network.jellyseerr.model

import com.divinelink.core.fixtures.core.network.jellyseerr.model.tv.TvSeasonResponseFactory
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse

object JellyseerrRequestMediaResponseFactory {

  /**
   * This is a movie with "available" REQUEST status and "processing" media.
   */
  fun movie(status: JellyseerrStatus.Media = JellyseerrStatus.Media.AVAILABLE) =
    JellyseerrRequestMediaResponse(
      message = null,
      type = "movie",
      status = status.status,
      media = JellyseerrRequestMediaResponse.MediaResponse(
        id = 123,
        status = JellyseerrStatus.Media.PROCESSING.status,
        tmdbId = 496244,
        requests = null,
      ),
      seasons = emptyList(),
      requestedBy = RequestedByResponseFactory.bob(),
      requestId = 12,
      createdAt = "2025-06-22T12:07:59.000Z",
    )

  /**
   * This is a partially available TV show, with a pending request.
   */
  fun partiallyAvailableTv() = JellyseerrRequestMediaResponse(
    message = null,
    type = "tv",
    status = JellyseerrStatus.Media.PENDING.status,
    media = JellyseerrRequestMediaResponse.MediaResponse(
      id = 134,
      tmdbId = 1399,
      requests = MediaInfoRequestResponseFactory.all(),
      status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE.status,
    ),
    seasons = TvSeasonResponseFactory.partially(),
    requestedBy = RequestedByResponseFactory.rhea(),
    requestId = 34,
    createdAt = "2025-06-22T12:07:59.000Z",
  )
}
