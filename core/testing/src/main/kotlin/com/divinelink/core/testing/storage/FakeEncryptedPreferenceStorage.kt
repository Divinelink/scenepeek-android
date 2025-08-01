package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.EncryptedStorage

open class FakeEncryptedPreferenceStorage(
  override var sessionId: String? = null,
  override var jellyseerrAuthCookie: String? = null,
  override var jellyseerrPassword: String? = null,
  override var accessToken: String? = null,
  override var tmdbAccountId: String? = null,
) : EncryptedStorage {

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

  override suspend fun clearAccessToken() {
    accessToken = null
  }

  override suspend fun setAccessToken(accessToken: String) {
    this.accessToken = accessToken
  }

  override suspend fun clearTmdbAccountId() {
    tmdbAccountId = null
  }

  override suspend fun setTmdbAccountId(accountId: String) {
    tmdbAccountId = accountId
  }
}
