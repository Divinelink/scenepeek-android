package com.divinelink.core.datastore

import com.divinelink.core.datastore.account.AccountStorage
import com.divinelink.core.model.account.AccountDetails
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class SessionStorage(
  val storage: PreferenceStorage,
  val encryptedStorage: EncryptedStorage,
  val accountStorage: AccountStorage,
) {
  val sessionId: String?
    get() = encryptedStorage.sessionId

  val accountId: Flow<String?>
    get() = accountStorage.accountId

  suspend fun setSession(sessionId: String) {
    encryptedStorage.setSessionId(sessionId)
  }

  suspend fun setTMDbAccountDetails(accountDetails: AccountDetails) {
    accountStorage.setAccountDetails(accountDetails)
  }

  suspend fun clearSession() {
    Timber.d("Cleared session.")
    encryptedStorage.clearSession()
    accountStorage.clearAccountDetails()
  }

  suspend fun setJellyseerrSession(
    username: String,
    address: String,
    authMethod: String,
    password: String,
  ) {
    storage.setJellyseerrAccount(username)
    storage.setJellyseerrAddress(address)
    storage.setJellyseerrAuthMethod(authMethod)
    encryptedStorage.setJellyseerrPassword(password)
  }

  suspend fun clearJellyseerrSession() {
    encryptedStorage.clearJellyseerrPassword()
    encryptedStorage.clearJellyseerrAuthCookie()
    storage.clearJellyseerrAccount()
    storage.clearJellyseerrSignInMethod()
    storage.clearJellyseerrAddress()
  }
}
