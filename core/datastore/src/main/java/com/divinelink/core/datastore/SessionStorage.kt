package com.divinelink.core.datastore

import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class SessionStorage(
  val storage: PreferenceStorage,
  val encryptedStorage: EncryptedStorage,
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

  suspend fun clearJellyseerrSession() {
    encryptedStorage.clearJellyseerrPassword()
    encryptedStorage.clearJellyseerrAuthCookie()
    storage.clearJellyseerrAccount()
    storage.clearJellyseerrSignInMethod()
    storage.clearJellyseerrAddress()
  }
}
