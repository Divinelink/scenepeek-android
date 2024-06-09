package com.andreolas.movierama.test.util.fakes

import com.andreolas.movierama.base.storage.EncryptedStorage

open class FakeEncryptedPreferenceStorage(
  override var tmdbAuthToken: String = "",
  override var sessionId: String? = null
) : EncryptedStorage {

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
