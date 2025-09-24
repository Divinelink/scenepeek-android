package com.divinelink.core.fixtures.core.network.jellyseerr.model.requests

import com.divinelink.core.fixtures.core.network.jellyseerr.model.MediaInfoRequestResponseFactory
import com.divinelink.core.fixtures.core.network.jellyseerr.model.RequestedByResponseFactory
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaResponse
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.PageInfoResponse
import com.divinelink.core.network.jellyseerr.model.requests.MediaRequestsResponse

object MediaRequestsResponseFactory {

  object All {
    val page1 = MediaRequestsResponse(
      pageInfo = PageInfoResponse(
        pages = 53,
        pageSize = 5,
        results = 264,
        page = 1,
      ),
      results = listOf(
        MediaInfoRequestResponse(
          id = 803,
          status = 2,
          media = JellyseerrRequestMediaResponse.MediaResponse(
            id = 581,
            mediaType = "movie",
            status = 3,
            tmdbId = 234,
            requests = null,
          ),
          createdAt = "2025-09-21T09:37:34.000Z",
          updatedAt = "2025-09-21T09:37:34.000Z",
          seasons = emptyList(),
          requestedBy = RequestedByResponseFactory.scenepeek,
          profileName = "SD",
          profileId = 2,
          canRemove = true,
          serverId = 0,
          rootFolder = "/data/media/movies",
        ),
        MediaInfoRequestResponse(
          id = 802,
          status = 1,
          media = JellyseerrRequestMediaResponse.MediaResponse(
            id = 678,
            mediaType = "movie",
            status = 2,
            tmdbId = 604079,
            requests = null,
          ),
          createdAt = "2025-09-20T10:55:18.000Z",
          updatedAt = "2025-09-20T10:55:18.000Z",
          seasons = emptyList(),
          requestedBy = RequestedByResponseFactory.scenepeek,
          profileName = null,
          canRemove = false,
          serverId = null,
          rootFolder = null,
          profileId = null,
        ),
        MediaInfoRequestResponse(
          id = 801,
          status = 1,
          media = JellyseerrRequestMediaResponse.MediaResponse(
            id = 657,
            mediaType = "movie",
            status = 2,
            tmdbId = 13494,
            requests = null,
          ),
          createdAt = "2025-09-20T10:55:10.000Z",
          updatedAt = "2025-09-20T10:55:10.000Z",
          seasons = emptyList(),
          requestedBy = RequestedByResponseFactory.scenepeek,
          profileName = null,
          canRemove = false,
          serverId = null,
          rootFolder = null,
          profileId = null,
        ),
        MediaInfoRequestResponse(
          id = 800,
          status = 2,
          media = JellyseerrRequestMediaResponse.MediaResponse(
            id = 677,
            mediaType = "movie",
            status = 3,
            tmdbId = 1242011,
            requests = null,
          ),
          createdAt = "2025-09-20T10:55:03.000Z",
          updatedAt = "2025-09-20T10:55:03.000Z",
          seasons = emptyList(),
          requestedBy = RequestedByResponseFactory.scenepeek,
          profileName = null,
          canRemove = true,
          serverId = null,
          rootFolder = null,
          profileId = null,
        ),
        MediaInfoRequestResponse(
          id = 799,
          status = 1,
          media = JellyseerrRequestMediaResponse.MediaResponse(
            id = 676,
            mediaType = "movie",
            status = 2,
            tmdbId = 914215,
            requests = null,
          ),
          createdAt = "2025-09-20T10:54:51.000Z",
          updatedAt = "2025-09-20T10:54:51.000Z",
          seasons = emptyList(),
          requestedBy = RequestedByResponseFactory.scenepeek,
          profileName = null,
          canRemove = false,
          serverId = null,
          rootFolder = null,
          profileId = null,
        ),
      ),
    )
  }

  val page1 = MediaRequestsResponse(
    pageInfo = PageInfoResponse(
      pages = 2,
      pageSize = 5,
      results = 10,
      page = 1,
    ),
    results = MediaInfoRequestResponseFactory.all(),
  )
}
