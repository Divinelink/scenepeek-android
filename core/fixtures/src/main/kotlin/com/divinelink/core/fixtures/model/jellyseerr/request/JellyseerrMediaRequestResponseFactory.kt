package com.divinelink.core.fixtures.model.jellyseerr.request

import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrMediaInfoFactory
import com.divinelink.core.model.jellyseerr.request.MediaRequestResult

object JellyseerrMediaRequestResponseFactory {

  fun movie() = MediaRequestResult(
    requestId = 12,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Movie.available(),
  )

  fun movieWithRequest() = MediaRequestResult(
    requestId = 12,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Movie.availableWithRequest(),
  )

  fun movieProcessing() = MediaRequestResult(
    requestId = 12,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Movie.processing(),
  )

  fun tv() = MediaRequestResult(
    requestId = 23,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.available(),
  )

  fun tvPartially() = MediaRequestResult(
    requestId = 34,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.partiallyAvailable(),
  )

  fun tvAllRequested() = MediaRequestResult(
    requestId = 45,
    message = null,
    mediaInfo = JellyseerrMediaInfoFactory.Tv.requested(),
  )

  fun movieFailure() = MediaRequestResult(
    requestId = 56,
    message = "Request failed",
    mediaInfo = JellyseerrMediaInfoFactory.Movie.unknown(),
  )

  fun tvFailure() = MediaRequestResult(
    requestId = 56,
    message = "Request failed",
    mediaInfo = JellyseerrMediaInfoFactory.Tv.unknown(),
  )
}
