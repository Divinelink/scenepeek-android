package com.divinelink.core.testing.network

import com.divinelink.core.datastore.auth.SavedState
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.network.client.JellyseerrRestClient
import com.divinelink.core.network.client.get
import com.divinelink.core.testing.storage.TestSavedStateStorage

class TestJellyseerrClient {

  lateinit var client: JellyseerrRestClient
  var storage: SavedStateStorage = TestSavedStateStorage()

  suspend fun withAccount(account: SavedState.JellyseerrCredentials) = apply {
    storage.setJellyseerrCredentials(account)
  }

  suspend inline fun <reified T : Any> mockGetResponse(
    url: String,
    json: String,
  ) {
    client = JellyseerrRestClient(
      engine = MockEngine(json),
      savedStateStorage = storage,
    )

    client.client.get<T>(url = url)
  }
}
