package com.divinelink.core.testing.factories.storage

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage

object SessionStorageFactory {

  fun noSessionId() = SessionStorage(
    storage = FakePreferenceStorage(),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = null),
    accountStorage = FakeAccountStorage(
      accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
    ),
  )

  fun noAccountId() = SessionStorage(
    storage = FakePreferenceStorage(),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = "123456789"),
    accountStorage = FakeAccountStorage(),
  )

  fun empty(
    storage: FakePreferenceStorage = FakePreferenceStorage(),
    encryptedStorage: FakeEncryptedPreferenceStorage = FakeEncryptedPreferenceStorage(),
    accountStorage: FakeAccountStorage = FakeAccountStorage(),
  ) = SessionStorage(
    storage = storage,
    encryptedStorage = encryptedStorage,
    accountStorage = accountStorage,
  )

  class SessionStorageWzd(private var sessionStorage: SessionStorage) {
    suspend fun withJellyseerrAddress(address: String) = apply {
      sessionStorage.storage.setJellyseerrAddress(address)
    }

    suspend fun withJellyseerrAccount(account: String) = apply {
      sessionStorage.storage.setJellyseerrAccount(account)
    }

    suspend fun withJellyseerrSignInMethod(signInMethod: String) = apply {
      sessionStorage.storage.setJellyseerrAuthMethod(signInMethod)
    }

    suspend fun withJellyseerrAuthCookie(cookie: String) = apply {
      sessionStorage.encryptedStorage.setJellyseerrAuthCookie(cookie)
    }

    fun create() = sessionStorage
  }

  suspend fun SessionStorage.toWzd(block: suspend SessionStorageWzd.() -> Unit) =
    SessionStorageWzd(this).apply { block() }.create()
}
