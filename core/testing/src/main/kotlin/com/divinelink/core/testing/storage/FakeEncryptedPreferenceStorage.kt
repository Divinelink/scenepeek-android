package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.EncryptedStorage

open class FakeEncryptedPreferenceStorage(
  override var tmdbAuthToken: String = "",
  override var sessionId: String? = null,
  override var jellyseerrAuthCookie: String? = null,
  override var jellyseerrPassword: String? = null,
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

  override suspend fun clearJellyseerrAuthCookie() {
    this.jellyseerrAuthCookie = null
  }

  override suspend fun setJellyseerrAuthCookie(cookie: String) {
    this.jellyseerrAuthCookie = cookie
  }

  override suspend fun clearJellyseerrPassword() {
    this.jellyseerrPassword = null
  }

  override suspend fun setJellyseerrPassword(password: String) {
    this.jellyseerrPassword = password
  }
}
