package com.divinelink.core.testing.network

import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.client.get
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage

class TestJellyseerrClient {

  lateinit var client: JellyseerrRestClient
  var storage: FakePreferenceStorage = FakePreferenceStorage()

  suspend fun withAddress(address: String) = apply {
    storage.setJellyseerrAddress(address)
  }

  suspend inline fun <reified T : Any> mockGetResponse(
    url: String,
    json: String,
  ) {
    client = JellyseerrRestClient(
      engine = MockEngine(json),
      encryptedStorage = FakeEncryptedPreferenceStorage(),
      storage = storage,
    )

    client.client.get<T>(url = url)
  }
}
