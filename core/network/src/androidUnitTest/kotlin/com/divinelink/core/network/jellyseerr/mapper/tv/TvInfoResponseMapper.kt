package com.divinelink.core.network.jellyseerr.mapper.tv

import JvmUnitTestDemoAssetManager
import com.divinelink.core.commons.extensions.toLocalDateTime
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequester
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.client.localJson
import com.divinelink.core.network.jellyseerr.model.tv.JellyseerrTvDetailsResponse
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test

class TvInfoResponseMapper {

  @Test
  fun `test TvInfoResponseMapper`() {
    JvmUnitTestDemoAssetManager
      .open("jellyseerr-tv-details-response.json")
      .use {
        val tvInfoResponseJson = it.readBytes().decodeToString().trimIndent()

        val serializer = JellyseerrTvDetailsResponse.serializer()
        val tvInfoResponse = localJson.decodeFromString(serializer, tvInfoResponseJson)

        val mappedMediaInfo = tvInfoResponse.mediaInfo?.map()
        mappedMediaInfo.toString() shouldBe JellyseerrMediaInfo(
          mediaId = 538,
          status = JellyseerrStatus.Media.PROCESSING,
          requests = listOf(
            JellyseerrRequest(
              id = 580,
              jellyseerrMediaId = 538,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(
                id = 6,
                displayName = "ScenePeek",
              ),
              requestDate = LocalDateTime(
                year = 2025, month = 6, day = 25, hour = 7, minute = 50, second = 29,
              ),
              seasons = listOf(
                SeasonRequest(seasonNumber = 5, status = JellyseerrStatus.Media.PENDING),
                SeasonRequest(seasonNumber = 6, status = JellyseerrStatus.Media.PENDING),
                SeasonRequest(seasonNumber = 7, status = JellyseerrStatus.Media.PENDING),
              ),
              media = MediaReference(
                mediaId = 44264,
                mediaType = MediaType.TV,
              ),
              profileName = null,
              profileId = null,
              canRemove = false,
            ),
            JellyseerrRequest(
              id = 593,
              jellyseerrMediaId = 538,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.FAILED,
              requester = JellyseerrRequester(
                id = 6,
                displayName = "ScenePeek",
              ),
              requestDate = "2025-06-25T13:45:39.000Z".toLocalDateTime(),
              seasons = listOf(
                SeasonRequest(seasonNumber = 9, status = JellyseerrStatus.Media.PENDING),
                SeasonRequest(seasonNumber = 8, status = JellyseerrStatus.Media.PENDING),
              ),
              media = MediaReference(
                mediaId = 44264,
                mediaType = MediaType.TV,
              ),
              profileName = null,
              profileId = null,
              canRemove = false,
            ),
            JellyseerrRequest(
              id = 594,
              jellyseerrMediaId = 538,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.DECLINED,
              requester = JellyseerrRequester(
                id = 6,
                displayName = "ScenePeek",
              ),
              requestDate = "2025-06-25T14:22:44.000Z".toLocalDateTime(),
              seasons = listOf(
                SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Media.UNKNOWN),
              ),
              media = MediaReference(
                mediaId = 44264,
                mediaType = MediaType.TV,
              ),
              profileName = null,
              profileId = null,
              canRemove = false,
            ),
            JellyseerrRequest(
              id = 602,
              jellyseerrMediaId = 538,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(
                id = 6,
                displayName = "ScenePeek",
              ),
              requestDate = LocalDateTime(
                year = 2025, month = 6, day = 25, hour = 15, minute = 25, second = 38,
              ),
              seasons = listOf(
                SeasonRequest(seasonNumber = 2, status = JellyseerrStatus.Media.PENDING),
              ),
              media = MediaReference(
                mediaId = 44264,
                mediaType = MediaType.TV,
              ),
              profileName = null,
              profileId = null,
              canRemove = false,
            ),
            JellyseerrRequest(
              id = 603,
              jellyseerrMediaId = 538,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(
                id = 6,
                displayName = "ScenePeek",
              ),
              requestDate = LocalDateTime(
                year = 2025, month = 6, day = 25, hour = 15, minute = 26, second = 3,
              ),
              seasons = listOf(
                SeasonRequest(seasonNumber = 1, status = JellyseerrStatus.Media.PENDING),
              ),
              media = MediaReference(
                mediaId = 44264,
                mediaType = MediaType.TV,
              ),
              profileName = null,
              profileId = null,
              canRemove = false,
            ),
            JellyseerrRequest(
              id = 626,
              jellyseerrMediaId = 538,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.APPROVED,
              requester = JellyseerrRequester(
                id = 1,
                displayName = "Admin",
              ),
              requestDate = LocalDateTime(
                year = 2025, month = 6, day = 26, hour = 8, minute = 34, second = 42,
              ),
              seasons = listOf(
                SeasonRequest(seasonNumber = 4, status = JellyseerrStatus.Media.PENDING),
              ),
              media = MediaReference(
                mediaId = 44264,
                mediaType = MediaType.TV,
              ),
              profileName = null,
              profileId = null,
              canRemove = false,
            ),
            JellyseerrRequest(
              id = 630,
              jellyseerrMediaId = 538,
              mediaStatus = JellyseerrStatus.Media.PROCESSING,
              requestStatus = JellyseerrStatus.Request.PENDING,
              requester = JellyseerrRequester(
                id = 6,
                displayName = "ScenePeek",
              ),
              requestDate = LocalDateTime(
                year = 2025, month = 6, day = 26, hour = 8, minute = 38, second = 2,
              ),
              seasons = listOf(
                SeasonRequest(seasonNumber = 3, status = JellyseerrStatus.Media.UNKNOWN),
              ),
              media = MediaReference(
                mediaId = 44264,
                mediaType = MediaType.TV,
              ),
              profileName = null,
              profileId = null,
              canRemove = false,
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
        ).toString()
      }
  }
}
