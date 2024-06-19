package com.divinelink.core.testing.factories.storage

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage

object SessionStorageFactory {

  fun noSessionId() = SessionStorage(
    storage = FakePreferenceStorage(
      accountId = "123456789"
    ),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = null)
  )

  fun noAccountId() = SessionStorage(
    storage = FakePreferenceStorage(
      accountId = null
    ),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = "123456789")
  )

  fun full() = SessionStorage(
    storage = FakePreferenceStorage(
      accountId = "123456789"
    ),
    encryptedStorage = FakeEncryptedPreferenceStorage(sessionId = "123456789")
  )
}
