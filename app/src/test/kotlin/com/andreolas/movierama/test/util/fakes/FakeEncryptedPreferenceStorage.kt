package com.andreolas.movierama.test.util.fakes

import com.divinelink.core.datastore.EncryptedStorage

open class FakeEncryptedPreferenceStorage(
  override var tmdbAuthToken: String = "",
  override var sessionId: String? = null
) : com.divinelink.core.datastore.EncryptedStorage {

  override suspend fun setTmdbAuthToken(key: String) {
    tmdbAuthToken = key
  }

  override suspend fun clearSession() {
    sessionId = null
  }

  override suspend fun setSessionId(sessionId: String) {
    this.sessionId = sessionId
  }
}
