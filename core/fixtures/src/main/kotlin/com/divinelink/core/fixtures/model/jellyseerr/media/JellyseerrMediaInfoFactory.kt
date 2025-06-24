package com.divinelink.core.fixtures.model.jellyseerr.media

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus

object JellyseerrMediaInfoFactory {

  object Movie {
    fun unknown() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = emptyList(),
      status = JellyseerrStatus.Media.UNKNOWN,
      seasons = emptyMap(),
    )

    fun available() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = emptyList(),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = emptyMap(),
    )

    fun availableWithRequest() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = listOf(JellyseerrRequestFactory.movie()),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = emptyMap(),
    )

    fun processing() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = emptyList(),
      status = JellyseerrStatus.Media.PROCESSING,
      seasons = emptyMap(),
    )

    fun pending() = JellyseerrMediaInfo(
      mediaId = 496244,
      requests = emptyList(),
      status = JellyseerrStatus.Media.PENDING,
      seasons = emptyMap(),
    )
  }

  object Tv {
    fun available() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = emptyList(),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = mapOf(
        1 to JellyseerrStatus.Media.AVAILABLE,
        2 to JellyseerrStatus.Media.AVAILABLE,
        3 to JellyseerrStatus.Media.AVAILABLE,
        4 to JellyseerrStatus.Media.AVAILABLE,
        5 to JellyseerrStatus.Media.AVAILABLE,
        6 to JellyseerrStatus.Media.AVAILABLE,
        7 to JellyseerrStatus.Media.AVAILABLE,
        8 to JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
        9 to JellyseerrStatus.Media.UNKNOWN,
      ),
    )

    fun requested() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = emptyList(),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = mapOf(
        1 to JellyseerrStatus.Media.AVAILABLE,
        2 to JellyseerrStatus.Media.AVAILABLE,
        3 to JellyseerrStatus.Media.AVAILABLE,
        4 to JellyseerrStatus.Media.AVAILABLE,
        5 to JellyseerrStatus.Media.AVAILABLE,
        6 to JellyseerrStatus.Media.AVAILABLE,
        7 to JellyseerrStatus.Media.AVAILABLE,
        8 to JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
        9 to JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
      ),
    )

    fun partiallyAvailable() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = JellyseerrRequestFactory.Tv.all(),
      status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
      seasons = mapOf(
        1 to JellyseerrStatus.Media.AVAILABLE,
        2 to JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
      ),
    )

    fun unknown() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = emptyList(),
      status = JellyseerrStatus.Media.UNKNOWN,
      seasons = mapOf(),
    )
  }
}
