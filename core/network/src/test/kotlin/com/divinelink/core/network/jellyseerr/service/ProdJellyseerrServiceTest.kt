package com.divinelink.core.network.jellyseerr.service

import com.divinelink.core.commons.domain.data
import com.divinelink.core.model.exception.MissingJellyseerrHostAddressException
import com.divinelink.core.network.jellyseerr.model.radarr.RadarrInstanceResponse
import com.divinelink.core.testing.factories.api.jellyseerr.response.radarr.RadarrInstanceResponseFactory
import com.divinelink.core.testing.factories.api.jellyseerr.response.sonarr.SonarrInstanceResponseFactory
import com.divinelink.core.testing.factories.json.jellyseerr.radarr.RadarrInstanceResponseJson
import com.divinelink.core.testing.factories.json.jellyseerr.sonarr.SonarrInstanceResponseJson
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
    restClient.withAddress("http://localhost:5055")

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
    restClient.withAddress("http://localhost:5055")

    restClient.mockGetResponse<List<RadarrInstanceResponse>>(
      url = "",
      json = SonarrInstanceResponseJson.success,
    )

    service = ProdJellyseerrService(
      restClient = restClient.client,
    )

    service.getSonarrInstances().data shouldBe SonarrInstanceResponseFactory.all
  }
}
