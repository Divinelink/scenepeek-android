package com.divinelink.core.fixtures.core.network.jellyseerr.model

import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.tv.TvSeasonResponse

object MediaInfoRequestResponseFactory {

  val together = MediaInfoRequestResponse(
    id = 800,
    status = JellyseerrStatus.Media.PENDING.status,
    media = JellyseerrRequestMediaResponse.MediaResponse(
      id = 677,
      mediaType = MediaType.MOVIE.value,
      status = 2,
      tmdbId = 1242011,
      requests = null,
    ),
    createdAt = "2025-09-20T10:55:03.000Z",
    updatedAt = "2025-09-20T10:55:03.000Z",
    seasons = emptyList(),
    requestedBy = RequestedByResponseFactory.scenepeek,
    profileName = null,
    canRemove = null,
  )

  fun betterCallSaul1() = MediaInfoRequestResponse(
    id = 2,
    status = JellyseerrStatus.Request.PENDING.status,
    seasons = listOf(
      TvSeasonResponse(
        seasonNumber = 2,
        status = JellyseerrStatus.Media.PENDING.status,
      ),
      TvSeasonResponse(
        seasonNumber = 3,
        status = JellyseerrStatus.Media.PENDING.status,
      ),
    ),
    createdAt = "2025-06-22T13:00:22.000Z",
    updatedAt = "2025-06-23T13:00:22.000Z",
    requestedBy = RequestedByResponseFactory.rhea(),
    media = JellyseerrRequestMediaResponse.MediaResponse(
      id = 1,
      status = JellyseerrStatus.Media.DELETED.status,
      tmdbId = 123,
      requests = emptyList(),
      mediaType = "tv",
    ),
  )

  fun betterCallSaul2() = MediaInfoRequestResponse(
    id = 3,
    status = JellyseerrStatus.Request.APPROVED.status,
    seasons = listOf(
      TvSeasonResponse(
        seasonNumber = 5,
        status = JellyseerrStatus.Media.PROCESSING.status,
      ),
    ),
    createdAt = "2025-06-21T13:00:22.000Z",
    updatedAt = "2025-06-24T13:00:22.000Z",
    requestedBy = RequestedByResponseFactory.rhea(),
    media = JellyseerrRequestMediaResponse.MediaResponse(
      id = 1,
      status = JellyseerrStatus.Media.PENDING.status,
      tmdbId = 123,
      requests = emptyList(),
      mediaType = "tv",
    ),
  )

  /**
   * DECLINED request
   */
  fun betterCallSaul3() = MediaInfoRequestResponse(
    id = 4,
    status = JellyseerrStatus.Request.DECLINED.status,
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
    createdAt = "2025-06-23T13:00:22.000Z",
    updatedAt = "2025-06-24T13:00:22.000Z",
    requestedBy = RequestedByResponseFactory.bob(),
    media = JellyseerrRequestMediaResponse.MediaResponse(
      id = 1,
      status = JellyseerrStatus.Media.AVAILABLE.status,
      tmdbId = 123,
      requests = emptyList(),
      mediaType = "tv",
    ),
  )

  fun all() = listOf(
    betterCallSaul1(),
    betterCallSaul2(),
    betterCallSaul3(),
  )
}
