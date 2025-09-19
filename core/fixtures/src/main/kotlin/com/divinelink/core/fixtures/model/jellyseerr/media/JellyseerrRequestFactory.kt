package com.divinelink.core.fixtures.model.jellyseerr.media

import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType

object JellyseerrRequestFactory {

  fun movie() = JellyseerrRequest(
    id = 1,
    jellyseerrMediaId = 5,
    mediaStatus = JellyseerrStatus.Media.AVAILABLE,
    requestStatus = JellyseerrStatus.Request.PENDING,
    requestDate = "June 22, 2025",
    requester = JellyseerrRequesterFactory.bob(),
    seasons = emptyList(),
    media = MediaReference(
      mediaId = 1234567,
      mediaType = MediaType.MOVIE,
    ),
    profileName = null,
    canRemove = false,
  )

  object Tv {
    fun betterCallSaul1() = JellyseerrRequest(
      id = 2,
      jellyseerrMediaId = 5,
      mediaStatus = JellyseerrStatus.Media.DELETED,
      requestStatus = JellyseerrStatus.Request.PENDING,
      requester = JellyseerrRequesterFactory.rhea(),
      requestDate = "June 22, 2025",
      seasons = listOf(
        SeasonRequest(seasonNumber = 2, status = JellyseerrStatus.Season.PENDING),
        SeasonRequest(seasonNumber = 3, status = JellyseerrStatus.Season.PENDING),
      ),
      media = MediaReference(
        mediaId = 1234567,
        mediaType = MediaType.TV,
      ),
      profileName = null,
      canRemove = false,
    )

    fun betterCallSaul2() = JellyseerrRequest(
      id = 3,
      jellyseerrMediaId = 5,
      mediaStatus = JellyseerrStatus.Media.PENDING,
      requestStatus = JellyseerrStatus.Request.APPROVED,
      requester = JellyseerrRequesterFactory.rhea(),
      requestDate = "June 21, 2025",
      seasons = listOf(
        SeasonRequest(seasonNumber = 5, status = JellyseerrStatus.Season.PROCESSING),
      ),
      media = MediaReference(
        mediaId = 1234567,
        mediaType = MediaType.TV,
      ),
      profileName = null,
      canRemove = false,
    )

    fun betterCallSaul3() = JellyseerrRequest(
      id = 4,
      jellyseerrMediaId = 5,
      mediaStatus = JellyseerrStatus.Media.AVAILABLE,
      requestStatus = JellyseerrStatus.Request.DECLINED,
      requester = JellyseerrRequesterFactory.bob(),
      requestDate = "June 23, 2025",
      seasons = listOf(
        SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Season.PROCESSING),
        SeasonRequest(seasonNumber = 6, status = JellyseerrStatus.Season.PENDING),
      ),
      media = MediaReference(
        mediaId = 1234567,
        mediaType = MediaType.TV,
      ),
      profileName = null,
      canRemove = false,
    )

    fun all() = listOf(
      betterCallSaul1(),
      betterCallSaul2(),
      betterCallSaul3(),
    )
  }
}
