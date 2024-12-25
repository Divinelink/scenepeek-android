package com.divinelink.core.testing.network

import JvmUnitTestDemoAssetManager
import com.divinelink.core.network.client.TMDbClient

class TestRestClient {

  lateinit var restClient: TMDbClient

  /**
   * Mocks the response of a GET request
   * @param jsonFileName The name of the JSON file to be used as the response
   * eg. "person-details.json"
   */
  suspend inline fun <reified T : Any> mockGetResponse(
    url: String,
    jsonFileName: String,
  ) {
    val json = JvmUnitTestDemoAssetManager.open(jsonFileName).use {
      it.readBytes().decodeToString().trimIndent()
    }

    restClient = TMDbClient(MockEngine(json))

    restClient.get<T>(url = url)
  }
}
