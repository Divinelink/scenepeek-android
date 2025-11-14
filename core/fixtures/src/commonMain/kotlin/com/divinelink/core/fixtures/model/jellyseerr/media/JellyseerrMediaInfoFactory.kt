package com.divinelink.core.fixtures.model.jellyseerr.media

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest

object JellyseerrMediaInfoFactory {

  object Movie {
    fun unknown() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = emptyList(),
      status = JellyseerrStatus.Media.UNKNOWN,
      seasons = emptyList(),
    )

    fun available() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = emptyList(),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = emptyList(),
    )

    fun availableWithRequest() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = listOf(JellyseerrRequestFactory.movie()),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = emptyList(),
    )

    fun processing() = JellyseerrMediaInfo(
      mediaId = 123,
      requests = emptyList(),
      status = JellyseerrStatus.Media.PROCESSING,
      seasons = emptyList(),
    )

    fun pending() = JellyseerrMediaInfo(
      mediaId = 496244,
      requests = emptyList(),
      status = JellyseerrStatus.Media.PENDING,
      seasons = emptyList(),
    )
  }

  object Tv {
    fun available() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = emptyList(),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = listOf(
        SeasonRequest(1, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(2, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(3, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(4, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(5, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(6, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(7, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(8, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
        SeasonRequest(9, JellyseerrStatus.Media.UNKNOWN),
      ),
    )

    fun requested() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = emptyList(),
      status = JellyseerrStatus.Media.AVAILABLE,
      seasons = listOf(
        SeasonRequest(1, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(2, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(3, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(4, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(5, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(6, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(7, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(8, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
        SeasonRequest(9, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
      ),
    )

    fun partiallyAvailable() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = JellyseerrRequestFactory.Tv.all(),
      status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
      seasons = listOf(
        SeasonRequest(1, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(2, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
      ),
    )

    fun emptyRequests() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = emptyList(),
      status = JellyseerrStatus.Media.PARTIALLY_AVAILABLE,
      seasons = listOf(
        SeasonRequest(1, JellyseerrStatus.Media.AVAILABLE),
        SeasonRequest(2, JellyseerrStatus.Media.PARTIALLY_AVAILABLE),
      ),
    )

    fun unknown() = JellyseerrMediaInfo(
      mediaId = 134,
      requests = emptyList(),
      status = JellyseerrStatus.Media.UNKNOWN,
      seasons = emptyList(),
    )
  }
}
