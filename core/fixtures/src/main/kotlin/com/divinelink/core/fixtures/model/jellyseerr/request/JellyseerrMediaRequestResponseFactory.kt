package com.divinelink.core.fixtures.model.jellyseerr.request

import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequestResponse

object JellyseerrMediaRequestResponseFactory {

  fun movie() = JellyseerrMediaRequestResponse(
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Movie.available(),
  )

  fun tv() = JellyseerrMediaRequestResponse(
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.available(),
  )

  fun tvPartially() = JellyseerrMediaRequestResponse(
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
  )

  fun tvAllRequested() = JellyseerrMediaRequestResponse(
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.requested(),
  )
}
