package com.divinelink.core.testing.network

import JvmUnitTestDemoAssetManager
import com.divinelink.core.fixtures.core.commons.PreviewBuildConfigProvider
import com.divinelink.core.network.client.AuthTMDbClient
import com.divinelink.core.network.client.get
import com.divinelink.core.testing.commons.provider.TestSecretProvider
import com.divinelink.core.testing.storage.TestSavedStateStorage

class TestAuthTMDbClient {

  lateinit var restClient: AuthTMDbClient
  val encryptedStorage = TestSavedStateStorage()

  suspend inline fun <reified T : Any, reified V : Any> mockPost(
    url: String,
    body: T,
    response: String,
  ) {
    restClient = AuthTMDbClient(
      engine = mockEngine(response),
      encryptedStorage = encryptedStorage,
      secret = TestSecretProvider(),
      config = PreviewBuildConfigProvider(),
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
      engine = mockEngine(json),
      encryptedStorage = encryptedStorage,
      secret = TestSecretProvider(),
      config = PreviewBuildConfigProvider(),
    )

    restClient.client.get<T>(url = url)
  }
}
