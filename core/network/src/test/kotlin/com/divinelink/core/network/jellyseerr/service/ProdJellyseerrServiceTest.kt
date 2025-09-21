package com.divinelink.core.network.jellyseerr.service

import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.fixtures.core.network.jellyseerr.model.MediaInfoRequestResponseFactory
import com.divinelink.core.fixtures.core.network.jellyseerr.model.requests.MediaRequestsResponseFactory
import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.model.filter.MediaRequestFilter
import com.divinelink.core.model.jellyseerr.request.RequestStatusUpdate
import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.requests.MediaRequestsResponse
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceDetailsResponse
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceResponse
import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceDetailsResponse
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.radarr.RadarrInstanceDetailsResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.radarr.RadarrInstanceResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.sonarr.SonarrInstanceDetailsResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.server.sonarr.SonarrInstanceResponseFactory
import com.divinelink.core.testing.factories.datastore.auth.JellyseerrAccountFactory
import com.divinelink.core.testing.factories.json.jellyseerr.model.MediaInfoRequestResponseJson
import com.divinelink.core.testing.factories.json.jellyseerr.model.MediaRequestsResponseJsonFactory
import com.divinelink.core.testing.factories.json.jellyseerr.server.radarr.RadarrInstanceDetailsResponseJson
import com.divinelink.core.testing.factories.json.jellyseerr.server.radarr.RadarrInstanceResponseJson
import com.divinelink.core.testing.factories.json.jellyseerr.server.sonarr.SonarrInstanceDetailsResponseJson
import com.divinelink.core.testing.factories.json.jellyseerr.server.sonarr.SonarrInstanceResponseJson
import com.divinelink.core.testing.network.TestJellyseerrClient
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProdJellyseerrServiceTest {

  private lateinit var service: ProdJellyseerrService
  private lateinit var restClient: TestJellyseerrClient

  @BeforeTest
  fun setup() {
    restClient = TestJellyseerrClient()
  }

  @Test
  fun `test getRadarrInstances without host address`() = runTest {
    restClient.mockGetResponse<List<RadarrInstanceResponse>>(
      url = "",
      json = RadarrInstanceResponseJson.success,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    val exception = shouldThrow<MissingJellyseerrHostAddressException> {
      service.getRadarrInstances().data
    }

    exception shouldBe MissingJellyseerrHostAddressException()
  }

  @Test
  fun `test getRadarrInstances with success`() = runTest {
    restClient.withAccount(JellyseerrAccountFactory.zabaob())

    restClient.mockGetResponse<List<RadarrInstanceResponse>>(
      url = "",
      json = RadarrInstanceResponseJson.success,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.getRadarrInstances().data shouldBe RadarrInstanceResponseFactory.all
  }

  @Test
  fun `test getSonarrInstances without host address`() = runTest {
    restClient.mockGetResponse<List<RadarrInstanceResponse>>(
      url = "",
      json = SonarrInstanceResponseJson.success,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    val exception = shouldThrow<MissingJellyseerrHostAddressException> {
      service.getSonarrInstances().data
    }

    exception shouldBe MissingJellyseerrHostAddressException()
  }

  @Test
  fun `test getSonarrInstances with success`() = runTest {
    restClient.withAccount(JellyseerrAccountFactory.zabaob())

    restClient.mockGetResponse<List<RadarrInstanceResponse>>(
      url = "",
      json = SonarrInstanceResponseJson.success,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.getSonarrInstances().data shouldBe SonarrInstanceResponseFactory.all
  }

  @Test
  fun `test getSonarrDetailsInstances with success`() = runTest {
    restClient.withAccount(JellyseerrAccountFactory.zabaob())

    restClient.mockGetResponse<SonarrInstanceDetailsResponse>(
      url = "",
      json = SonarrInstanceDetailsResponseJson.default,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.getSonarrInstanceDetails(id = 0).data shouldBe
      SonarrInstanceDetailsResponseFactory.default
  }

  @Test
  fun `test getSonarrDetailsInstances without host address`() = runTest {
    restClient.mockGetResponse<SonarrInstanceDetailsResponse>(
      url = "",
      json = SonarrInstanceDetailsResponseJson.default,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    val exception = shouldThrow<MissingJellyseerrHostAddressException> {
      service.getSonarrInstanceDetails(0).data
    }

    exception shouldBe MissingJellyseerrHostAddressException()
  }

  @Test
  fun `test getRadarrDetailsInstances with success`() = runTest {
    restClient.withAccount(JellyseerrAccountFactory.zabaob())

    restClient.mockGetResponse<RadarrInstanceDetailsResponse>(
      url = "",
      json = RadarrInstanceDetailsResponseJson.default,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.getRadarrInstanceDetails(id = 0).data shouldBe
      RadarrInstanceDetailsResponseFactory.default
  }

  @Test
  fun `test getRadarrDetailsInstances without host address`() = runTest {
    restClient.mockGetResponse<RadarrInstanceDetailsResponse>(
      url = "",
      json = RadarrInstanceDetailsResponseJson.default,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    val exception = shouldThrow<MissingJellyseerrHostAddressException> {
      service.getRadarrInstanceDetails(0).data
    }

    exception shouldBe MissingJellyseerrHostAddressException()
  }

  @Test
  fun `test retryRequest with success`() = runTest {
    restClient.withAccount(JellyseerrAccountFactory.zabaob())

    restClient.mockPostResponse<MediaInfoRequestResponse>(
      url = "",
      json = MediaInfoRequestResponseJson.together,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.retryRequest(800).data shouldBe MediaInfoRequestResponseFactory.together
  }

  @Test
  fun `test retryRequest without host address`() = runTest {
    restClient.mockGetResponse<MediaInfoRequestResponse>(
      url = "",
      json = MediaInfoRequestResponseJson.together,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    val exception = shouldThrow<MissingJellyseerrHostAddressException> {
      service.retryRequest(800).data
    }

    exception shouldBe MissingJellyseerrHostAddressException()
  }

  @Test
  fun `test updateRequestStatus with success`() = runTest {
    restClient.withAccount(JellyseerrAccountFactory.zabaob())

    restClient.mockPostResponse<MediaInfoRequestResponse>(
      url = "",
      json = MediaInfoRequestResponseJson.together,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.updateRequestStatus(
      requestId = 800,
      status = RequestStatusUpdate.APPROVE,
    ).data shouldBe MediaInfoRequestResponseFactory.together
  }

  @Test
  fun `test updateRequestStatus without host address`() = runTest {
    restClient.mockPostResponse<MediaInfoRequestResponse>(
      url = "",
      json = MediaInfoRequestResponseJson.together,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    val exception = shouldThrow<MissingJellyseerrHostAddressException> {
      service.updateRequestStatus(
        requestId = 800,
        status = RequestStatusUpdate.APPROVE,
      ).data
    }

    exception shouldBe MissingJellyseerrHostAddressException()
  }

  @Test
  fun `test getRequests with success`() = runTest {
    restClient.withAccount(JellyseerrAccountFactory.zabaob())

    restClient.mockGetResponse<MediaRequestsResponse>(
      url = "",
      json = MediaRequestsResponseJsonFactory.All.page1,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.getRequests(
      skip = 0,
      filter = MediaRequestFilter.All,
    ).test {
      awaitItem() shouldBe Result.success(MediaRequestsResponseFactory.All.page1)
      awaitComplete()
    }
  }

  @Test
  fun `test getRequests without host address`() = runTest {
    restClient.mockGetResponse<MediaRequestsResponse>(
      url = "",
      json = MediaRequestsResponseJsonFactory.All.page1,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.getRequests(
      skip = 0,
      filter = MediaRequestFilter.All,
    ).test {
      awaitError() shouldBe MissingJellyseerrHostAddressException()
    }
  }
}
