package com.divinelink.core.testing.network

import JvmUnitTestDemoAssetManager
import com.divinelink.core.network.client.OMDbClient
import com.divinelink.core.network.client.get

class TestOMDbClient {

  lateinit var restClient: OMDbClient

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

    restClient = OMDbClient(MockEngine(json))

    restClient.client.get<T>(url = url)
  }

  suspend inline fun <reified T : Any> mockGetResponseJson(
    url: String,
    json: String,
  ) {
    restClient = OMDbClient(MockEngine(json))

    restClient.client.get<T>(url = url)
  }
}
