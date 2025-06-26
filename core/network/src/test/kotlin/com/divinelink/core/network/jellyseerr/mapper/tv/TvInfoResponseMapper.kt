package com.divinelink.core.network.jellyseerr.mapper.tv

import JvmUnitTestDemoAssetManager
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequester
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class TvInfoResponseMapper {

  @Test
  fun `test TvInfoResponseMapper`() = JvmUnitTestDemoAssetManager
    .open("jellyseerr-tv-details-response.json")
    .use {
      val tvInfoResponseJson = it.readBytes().decodeToString().trimIndent()

      val serializer = JellyseerrTvDetailsResponse.serializer()
      val tvInfoResponse = localJson.decodeFromString(serializer, tvInfoResponseJson)

      val mappedMediaInfo = tvInfoResponse.mediaInfo?.map()

      assertThat(mappedMediaInfo).isInstanceOf(
        JellyseerrMediaInfo(
          mediaId = 538,
          status = JellyseerrStatus.Media.PROCESSING,
          requests = listOf(
            JellyseerrRequest(
              id = 580,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(displayName = "ScenePeek"),
              requestDate = "June 25, 2025",
              seasons = listOf(
                SeasonRequest(seasonNumber = 5, status = JellyseerrStatus.Media.PENDING),
                SeasonRequest(seasonNumber = 6, status = JellyseerrStatus.Media.PENDING),
                SeasonRequest(seasonNumber = 7, status = JellyseerrStatus.Media.PENDING),
              ),
            ),
            JellyseerrRequest(
              id = 593,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.FAILED,
              requester = JellyseerrRequester(displayName = "ScenePeek"),
              requestDate = "June 25, 2025",
              seasons = listOf(
                SeasonRequest(seasonNumber = 9, status = JellyseerrStatus.Media.PENDING),
                SeasonRequest(seasonNumber = 8, status = JellyseerrStatus.Media.PENDING),
              ),
            ),
            JellyseerrRequest(
              id = 594,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.DECLINED,
              requester = JellyseerrRequester(displayName = "ScenePeek"),
              requestDate = "June 25, 2025",
              seasons = listOf(
                SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Media.UNKNOWN),
              ),
            ),
            JellyseerrRequest(
              id = 602,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(displayName = "ScenePeek"),
              requestDate = "June 25, 2025",
              seasons = listOf(
                SeasonRequest(seasonNumber = 2, status = JellyseerrStatus.Media.PENDING),
              ),
            ),
            JellyseerrRequest(
              id = 603,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(displayName = "ScenePeek"),
              requestDate = "June 25, 2025",
              seasons = listOf(
                SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Media.PENDING),
              ),
            ),
            JellyseerrRequest(
              id = 626,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(displayName = "Admin"),
              requestDate = "June 26, 2025",
              seasons = listOf(
                SeasonRequest(seasonNumber = 4, status = JellyseerrStatus.Media.PENDING),
              ),
            ),
            JellyseerrRequest(
              id = 630,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.PENDING,
              requester = JellyseerrRequester(displayName = "ScenePeek"),
              requestDate = "June 26, 2025",
              seasons = listOf(
                SeasonRequest(seasonNumber = 3, status = JellyseerrStatus.Media.UNKNOWN),
              ),
            ),
          ),
          seasons = listOf(
            SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(seasonNumber = 2, status = JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(seasonNumber = 3, status = JellyseerrStatus.Media.PENDING),
            SeasonRequest(seasonNumber = 4, status = JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(seasonNumber = 5, status = JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(seasonNumber = 6, status = JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(seasonNumber = 7, status = JellyseerrStatus.Media.PROCESSING),
            SeasonRequest(seasonNumber = 8, status = JellyseerrStatus.Request.FAILED),
            SeasonRequest(seasonNumber = 9, status = JellyseerrStatus.Request.FAILED),
          ),
        )::class.java,
      )
    }
}
