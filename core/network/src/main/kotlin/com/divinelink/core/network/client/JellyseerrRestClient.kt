package com.divinelink.core.network.client

import com.divinelink.core.datastore.EncryptedStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.cookies.HttpCookies
import javax.inject.Inject

class JellyseerrRestClient @Inject constructor(
  engine: HttpClientEngine,
  private val encryptedStorage: EncryptedStorage,
) {

  val client: HttpClient = ktorClient(engine).config {
    install(HttpCookies) {
      storage = PersistentCookieStorage(encryptedStorage)
    }
  }

  suspend inline fun <reified T : Any> get(url: String): T = client.get(url)

  suspend inline fun <reified T : Any, reified V : Any> post(
    url: String,
    body: T,
  ): V = client.post(url, body)

  fun close() {
    client.close()
  }
}
