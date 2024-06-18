package com.divinelink.core.datastore

import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStorage @Inject constructor(
  private val storage: PreferenceStorage,
  private val encryptedStorage: EncryptedStorage
) {

  val sessionId: String?
    get() = encryptedStorage.sessionId

  val accountId: Flow<String?>
    get() = storage.accountId

  suspend fun clearToken() {
    storage.clearToken()
  }

  suspend fun setToken(token: String) {
    storage.setToken(token)
  }

  suspend fun setSession(sessionId: String) {
    encryptedStorage.setSessionId(sessionId)
    storage.setHasSession(true)
    storage.clearToken()
  }

  suspend fun clearSession() {
    Timber.d("Cleared session.")
    encryptedStorage.clearSession()
    storage.setHasSession(false)
    storage.clearToken()
    storage.clearAccountId()
  }

  suspend fun setAccountId(accountId: String) {
    storage.setAccountId(accountId)
  }
}