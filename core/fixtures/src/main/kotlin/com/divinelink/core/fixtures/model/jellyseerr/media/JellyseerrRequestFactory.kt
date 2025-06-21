package com.divinelink.core.fixtures.model.jellyseerr.media

import com.divinelink.core.commons.Constants
import com.divinelink.core.commons.extensions.formatTo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest

object JellyseerrRequestFactory {

  fun movie() = JellyseerrRequest.Movie(
    id = 1,
    status = JellyseerrMediaStatus.PENDING,
    requestDate = "2025-06-19T14:52:56.000Z".formatTo(
      inputFormat = Constants.ISO_8601,
      outputFormat = Constants.MMMM_DD_YYYY,
    )!!,
    requester = JellyseerrRequesterFactory.bob(),
  )

  object Tv {
    fun betterCallSaul1() = JellyseerrRequest.TV(
      id = 2,
      status = JellyseerrMediaStatus.PENDING,
      requester = JellyseerrRequesterFactory.rhea(),
      requestDate = "2025-06-19T14:52:56.000Z".formatTo(
        inputFormat = Constants.ISO_8601,
        outputFormat = Constants.MMMM_DD_YYYY,
      )!!,
      seasons = listOf(1),
    )

    fun betterCallSaul2() = JellyseerrRequest.TV(
      id = 3,
      status = JellyseerrMediaStatus.AVAILABLE,
      requester = JellyseerrRequesterFactory.rhea(),
      requestDate = "2025-06-20T14:52:56.000Z".formatTo(
        inputFormat = Constants.ISO_8601,
        outputFormat = Constants.MMMM_DD_YYYY,
      )!!,
      seasons = listOf(2, 3, 4),
    )

    fun betterCallSaul3() = JellyseerrRequest.TV(
      id = 4,
      status = JellyseerrMediaStatus.PROCESSING,
      requester = JellyseerrRequesterFactory.bob(),
      requestDate = "2025-06-21T14:52:56.000Z".formatTo(
        inputFormat = Constants.ISO_8601,
        outputFormat = Constants.MMMM_DD_YYYY,
      )!!,
      seasons = listOf(5, 6),
    )

    fun all() = listOf(
      betterCallSaul1(),
      betterCallSaul2(),
      betterCallSaul3(),
    )
  }
}
