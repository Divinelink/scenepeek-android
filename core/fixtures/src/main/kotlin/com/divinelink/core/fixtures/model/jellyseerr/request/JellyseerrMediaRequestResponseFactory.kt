package com.divinelink.core.fixtures.model.jellyseerr.request

import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequestResponse

object JellyseerrMediaRequestResponseFactory {

  fun movie() = JellyseerrMediaRequestResponse(
    requestId = 12,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Movie.available(),
  )

  fun tv() = JellyseerrMediaRequestResponse(
    requestId = 23,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.available(),
  )

  fun tvPartially() = JellyseerrMediaRequestResponse(
    requestId = 34,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
  )

  fun tvAllRequested() = JellyseerrMediaRequestResponse(
    requestId = 45,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.requested(),
  )
}
