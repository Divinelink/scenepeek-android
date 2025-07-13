package com.divinelink.core.testing.network

import JvmUnitTestDemoAssetManager
import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.client.get
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage

class TestAuthTMDbClient {

  lateinit var restClient: AuthTMDbClient
  val encryptedStorage = FakeEncryptedPreferenceStorage()

  suspend inline fun <reified T : Any, reified V : Any> mockPost(
    url: String,
    body: T,
    response: String,
  ) {
    restClient = AuthTMDbClient(
      engine = MockEngine(response),
      encryptedStorage = encryptedStorage,
    )

    return restClient.post(url = url, body = body)
  }

  suspend inline fun <reified T : Any> mockGet(
    url: String,
    jsonFileName: String,
  ) {
    val json = JvmUnitTestDemoAssetManager.open(jsonFileName).use {
      it.readBytes().decodeToString().trimIndent()
    }

    restClient = AuthTMDbClient(
      engine = MockEngine(json),
      encryptedStorage = encryptedStorage,
    )

    restClient.client.get<T>(url = url)
  }
}
