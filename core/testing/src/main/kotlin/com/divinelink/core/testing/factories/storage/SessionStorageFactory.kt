package com.divinelink.core.testing.factories.storage

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage

object SessionStorageFactory {

  fun noSessionId() = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = null,
      tmdbAccountId = AccessTokenFactory.valid().accountId,
      accessToken = AccessTokenFactory.valid().accessToken,
    ),
    accountStorage = FakeAccountStorage(
      accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
    ),
  )

  fun noAccountId() = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = "123456789"),
    accountStorage = FakeAccountStorage(
      accountDetails = AccountDetailsFactory.Pinkman(),
    ),
  )

  fun full() = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = "123456789",
      tmdbAccountId = AccessTokenFactory.valid().accountId,
      accessToken = AccessTokenFactory.valid().accessToken,
    ),
    accountStorage = FakeAccountStorage(
      accountDetails = AccountDetailsFactory.Pinkman().copy(id = 123456789),
    ),
  )

  fun empty(
    encryptedStorage: FakeEncryptedPreferenceStorage = FakeEncryptedPreferenceStorage(),
    accountStorage: FakeAccountStorage = FakeAccountStorage(),
  ) = SessionStorage(
    encryptedStorage = encryptedStorage,
    accountStorage = accountStorage,
  )
}
