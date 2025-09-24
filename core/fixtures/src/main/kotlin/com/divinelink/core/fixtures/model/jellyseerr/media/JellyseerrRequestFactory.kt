package com.divinelink.core.fixtures.model.jellyseerr.media

import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceProfileFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.InstanceRootFolderFactory
import com.divinelink.core.fixtures.model.jellyseerr.server.sonarr.SonarrInstanceFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.toStub

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
    profileId = null,
    canRemove = false,
  )

  object Tv {
    fun betterCallSaul1() = JellyseerrRequest(
      id = 2,
      jellyseerrMediaId = 1,
      mediaStatus = JellyseerrStatus.Media.DELETED,
      requestStatus = JellyseerrStatus.Request.PENDING,
      requester = JellyseerrRequesterFactory.rhea(),
      requestDate = "June 22, 2025",
      seasons = listOf(
        SeasonRequest(seasonNumber = 2, status = JellyseerrStatus.Season.PENDING),
        SeasonRequest(seasonNumber = 3, status = JellyseerrStatus.Season.PENDING),
      ),
      media = MediaReference(
        mediaId = 123,
        mediaType = MediaType.TV,
      ),
      profileName = null,
      profileId = null,
      canRemove = false,
    )

    fun betterCallSaul2() = JellyseerrRequest(
      id = 3,
      jellyseerrMediaId = 1,
      mediaStatus = JellyseerrStatus.Media.PENDING,
      requestStatus = JellyseerrStatus.Request.APPROVED,
      requester = JellyseerrRequesterFactory.rhea(),
      requestDate = "June 21, 2025",
      seasons = listOf(
        SeasonRequest(seasonNumber = 5, status = JellyseerrStatus.Season.PROCESSING),
      ),
      media = MediaReference(
        mediaId = 123,
        mediaType = MediaType.TV,
      ),
      profileName = null,
      profileId = null,
      canRemove = false,
    )

    fun betterCallSaul3() = JellyseerrRequest(
      id = 4,
      jellyseerrMediaId = 1,
      mediaStatus = JellyseerrStatus.Media.AVAILABLE,
      requestStatus = JellyseerrStatus.Request.DECLINED,
      requester = JellyseerrRequesterFactory.bob(),
      requestDate = "June 23, 2025",
      seasons = listOf(
        SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Season.PROCESSING),
        SeasonRequest(seasonNumber = 6, status = JellyseerrStatus.Season.PENDING),
      ),
      media = MediaReference(
        mediaId = 123,
        mediaType = MediaType.TV,
      ),
      profileName = null,
      profileId = null,
      canRemove = false,
    )

    fun theOffice1() = JellyseerrRequest(
      id = 5,
      jellyseerrMediaId = 2,
      media = MediaItemFactory.theOffice().toStub(),
      mediaStatus = JellyseerrStatus.Media.PENDING,
      requestStatus = JellyseerrStatus.Request.APPROVED,
      requester = JellyseerrRequesterFactory.scenepeek,
      requestDate = "Sept 25, 2025",
      seasons = listOf(
        SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Season.PENDING),
        SeasonRequest(seasonNumber = 6, status = JellyseerrStatus.Season.PENDING),
      ),
      canRemove = false,
      serverId = SonarrInstanceFactory.anime.id,
      profileId = InstanceProfileFactory.any.id,
      rootFolder = InstanceRootFolderFactory.anime.path,
      profileName = InstanceProfileFactory.any.name,
    )

    fun all() = listOf(
      betterCallSaul1(),
      betterCallSaul2(),
      betterCallSaul3(),
    )
  }
}
