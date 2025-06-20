package com.divinelink.core.fixtures.core.network.jellyseerr.model.movie

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.network.jellyseerr.model.movie.MovieInfoResponse

object MovieInfoResponseFactory {

  fun pending() = MovieInfoResponse(
    id = 496244,
    status = JellyseerrMediaStatus.PENDING.status,
    requests = emptyList(),
  )
}
