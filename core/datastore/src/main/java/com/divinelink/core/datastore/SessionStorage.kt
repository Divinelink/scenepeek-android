package com.divinelink.core.datastore

import com.divinelink.core.datastore.account.AccountStorage
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.session.AccessToken
import timber.log.Timber

class SessionStorage(
  val storage: PreferenceStorage,
  val encryptedStorage: EncryptedStorage,
  val accountStorage: AccountStorage,
) {
  val sessionId: String?
    get() = encryptedStorage.sessionId

  // V4 Account Object ID
  val accountId: String?
    get() = encryptedStorage.tmdbAccountId

  suspend fun setTMDbAccountDetails(accountDetails: AccountDetails) {
    accountStorage.setAccountDetails(accountDetails)
  }

  suspend fun setAccessToken(
    sessionId: String,
    accessToken: AccessToken,
  ) {
    encryptedStorage.setSessionId(sessionId)
    encryptedStorage.setAccessToken(accessToken.accessToken)
    encryptedStorage.setTmdbAccountId(accessToken.accountId)
  }

  suspend fun clearSession() {
    Timber.d("Cleared session.")
    encryptedStorage.clearSession()
    encryptedStorage.clearAccessToken()
    encryptedStorage.clearTmdbAccountId()
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
