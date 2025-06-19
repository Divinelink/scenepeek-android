package com.divinelink.core.fixtures.model.jellyseerr.media

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaStatus

object JellyseerrMediaInfoFactory {

  object Movie {
    fun available() = JellyseerrMediaInfo(
      mediaId = 496244,
      requests = emptyList(),
      status = JellyseerrMediaStatus.AVAILABLE,
      seasons = emptyMap(),
    )

    fun pending() = JellyseerrMediaInfo(
      mediaId = 496244,
      requests = emptyList(),
      status = JellyseerrMediaStatus.PENDING,
      seasons = emptyMap(),
    )
  }

  fun tv() = JellyseerrMediaInfo(
    mediaId = 1399,
    requests = emptyList(),
    status = JellyseerrMediaStatus.AVAILABLE,
    seasons = mapOf(
      1 to JellyseerrMediaStatus.AVAILABLE,
      2 to JellyseerrMediaStatus.AVAILABLE,
      3 to JellyseerrMediaStatus.AVAILABLE,
      4 to JellyseerrMediaStatus.AVAILABLE,
      5 to JellyseerrMediaStatus.AVAILABLE,
      6 to JellyseerrMediaStatus.AVAILABLE,
      7 to JellyseerrMediaStatus.AVAILABLE,
      8 to JellyseerrMediaStatus.PARTIALLY_AVAILABLE,
      9 to JellyseerrMediaStatus.UNKNOWN,
    ),
  )

  fun tvPartiallyAvailable() = JellyseerrMediaInfo(
    mediaId = 1399,
    requests = emptyList(),
    status = JellyseerrMediaStatus.PARTIALLY_AVAILABLE,
    seasons = mapOf(
      1 to JellyseerrMediaStatus.AVAILABLE,
      2 to JellyseerrMediaStatus.PARTIALLY_AVAILABLE,
    ),
  )
}
