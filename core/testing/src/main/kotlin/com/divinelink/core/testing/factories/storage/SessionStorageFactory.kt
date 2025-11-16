package com.divinelink.core.testing.factories.storage

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage

object SessionStorageFactory {

  fun noSessionId() = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = null,
      tmdbAccountId = AccessTokenFactory.valid().accountId,
      accessToken = AccessTokenFactory.valid().accessToken,
    ),
  )

  fun noAccountId() = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = "123456789"),
  )

  fun full() = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = "123456789",
      tmdbAccountId = AccessTokenFactory.valid().accountId,
      accessToken = AccessTokenFactory.valid().accessToken,
    ),
  )

  fun empty(
    encryptedStorage: FakeEncryptedPreferenceStorage = FakeEncryptedPreferenceStorage(),
  ) = SessionStorage(
    encryptedStorage = encryptedStorage,
  )
}
