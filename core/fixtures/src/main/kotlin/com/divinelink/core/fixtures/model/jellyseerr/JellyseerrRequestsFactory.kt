package com.divinelink.core.fixtures.model.jellyseerr

import com.divinelink.core.fixtures.model.jellyseerr.media.JellyseerrRequesterFactory
import com.divinelink.core.model.jellyseerr.JellyseerrRequests
import com.divinelink.core.model.jellyseerr.PageInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType

object JellyseerrRequestsFactory {

  object All {
    val page1 = JellyseerrRequests(
      pageInfo = PageInfo(
        pages = 53,
        pageSize = 5,
        results = 264,
        page = 1,
      ),
      results = listOf(
        JellyseerrRequest(
          id = 803,
          jellyseerrMediaId = 581,
          media = MediaReference(
            mediaId = 234,
            mediaType = MediaType.MOVIE,
          ),
          mediaStatus = JellyseerrStatus.Media.PROCESSING,
          requestStatus = JellyseerrStatus.Request.APPROVED,
          requester = JellyseerrRequesterFactory.scenepeek,
          requestDate = "September 21, 2025",
          seasons = emptyList(),
          profileName = "SD",
          profileId = 2,
          canRemove = true,
        ),
        JellyseerrRequest(
          id = 802,
          jellyseerrMediaId = 678,
          media = MediaReference(
            mediaId = 604079,
            mediaType = MediaType.MOVIE,
          ),
          mediaStatus = JellyseerrStatus.Media.PENDING,
          requestStatus = JellyseerrStatus.Request.PENDING,
          requester = JellyseerrRequesterFactory.scenepeek,
          requestDate = "September 20, 2025",
          seasons = emptyList(),
          profileName = null,
          profileId = null,
          canRemove = false,
        ),
        JellyseerrRequest(
          id = 801,
          jellyseerrMediaId = 657,
          media = MediaReference(
            mediaId = 13494,
            mediaType = MediaType.MOVIE,
          ),
          mediaStatus = JellyseerrStatus.Media.PENDING,
          requestStatus = JellyseerrStatus.Request.PENDING,
          requester = JellyseerrRequesterFactory.scenepeek,
          requestDate = "September 20, 2025",
          seasons = emptyList(),
          profileName = null,
          profileId = null,
          canRemove = false,
        ),
        JellyseerrRequest(
          id = 800,
          jellyseerrMediaId = 677,
          media = MediaReference(
            mediaId = 1242011,
            mediaType = MediaType.MOVIE,
          ),
          mediaStatus = JellyseerrStatus.Media.PROCESSING,
          requestStatus = JellyseerrStatus.Request.APPROVED,
          requester = JellyseerrRequesterFactory.scenepeek,
          requestDate = "September 20, 2025",
          seasons = emptyList(),
          profileName = null,
          profileId = null,
          canRemove = true,
        ),
        JellyseerrRequest(
          id = 799,
          jellyseerrMediaId = 676,
          media = MediaReference(
            mediaId = 914215,
            mediaType = MediaType.MOVIE,
          ),
          mediaStatus = JellyseerrStatus.Media.PENDING,
          requestStatus = JellyseerrStatus.Request.PENDING,
          requester = JellyseerrRequesterFactory.scenepeek,
          requestDate = "September 20, 2025",
          seasons = emptyList(),
          profileName = null,
          profileId = null,
          canRemove = false,
        ),

      ),
    )
  }
}
