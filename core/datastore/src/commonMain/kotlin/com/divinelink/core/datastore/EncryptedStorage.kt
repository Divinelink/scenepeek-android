package com.divinelink.core.datastore

interface EncryptedStorage {
  suspend fun clearSession()
  suspend fun setSessionId(sessionId: String)
  val sessionId: String?

  suspend fun clearAccessToken()
  suspend fun setAccessToken(accessToken: String)
  val accessToken: String?

  suspend fun clearTmdbAccountId()
  suspend fun setTmdbAccountId(accountId: String)
  val tmdbAccountId: String?
}
