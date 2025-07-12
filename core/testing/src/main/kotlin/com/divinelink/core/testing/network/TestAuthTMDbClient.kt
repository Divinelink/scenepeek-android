package com.divinelink.core.testing.network

import com.divinelink.core.network.client.AuthTMDbClient
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
}
