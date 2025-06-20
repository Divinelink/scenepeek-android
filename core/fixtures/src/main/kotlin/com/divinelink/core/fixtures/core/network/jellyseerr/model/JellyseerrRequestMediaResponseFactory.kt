package com.divinelink.core.fixtures.core.network.jellyseerr.model

import com.divinelink.core.fixtures.core.network.jellyseerr.model.tv.TvSeasonResponseFactory
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse

object JellyseerrRequestMediaResponseFactory {

  fun movie(status: JellyseerrMediaStatus = JellyseerrMediaStatus.AVAILABLE) =
    JellyseerrRequestMediaResponse(
      message = null,
      type = "movie",
      status = status.status,
      media = JellyseerrRequestMediaResponse.MediaResponse(496244),
      seasons = emptyList(),
    )

  fun tv() = JellyseerrRequestMediaResponse(
    message = null,
    type = "tv",
    status = JellyseerrMediaStatus.PARTIALLY_AVAILABLE.status,
    media = JellyseerrRequestMediaResponse.MediaResponse(1399),
    seasons = TvSeasonResponseFactory.partially(),
  )
}
