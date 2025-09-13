package com.divinelink.core.datastore

import com.divinelink.core.datastore.account.AccountStorage
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.session.AccessToken

class SessionStorage(
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
    encryptedStorage.clearSession()
    encryptedStorage.clearAccessToken()
    encryptedStorage.clearTmdbAccountId()
    accountStorage.clearAccountDetails()
  }
}
