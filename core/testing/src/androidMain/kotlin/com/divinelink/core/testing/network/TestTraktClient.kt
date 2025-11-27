package com.divinelink.core.testing.network

import com.divinelink.core.testing.commons.provider.TestSecretProvider
import com.divinelink.core.fixtures.core.commons.PreviewBuildConfigProvider
import com.divinelink.core.network.client.TraktClient
import com.divinelink.core.network.client.get

class TestTraktClient {

  lateinit var client: TraktClient

  suspend inline fun <reified T : Any> mockGetResponse(
    url: String,
    json: String,
  ) {
    client = TraktClient(
      engine = MockEngine(json),
      config = PreviewBuildConfigProvider(),
      secret = TestSecretProvider(),
    )

    client.client.get<T>(url = url)
  }
}
