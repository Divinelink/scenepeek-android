package com.divinelink.core.testing.factories.storage

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.fixtures.model.session.TmdbSessionFactory
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.testing.storage.TestSavedStateStorage

object SessionStorageFactory {

  fun empty() = SessionStorage(
    savedState = TestSavedStateStorage(
      tmdbSession = null,
    ),
  )

  fun full(accessToken: AccessToken = AccessTokenFactory.valid()) = SessionStorage(
    savedState = TestSavedStateStorage(
      tmdbSession = TmdbSessionFactory.full().copy(
        accessToken = accessToken,
      ),
    ),
  )

  fun empty(savedState: SavedStateStorage = TestSavedStateStorage()) = SessionStorage(
    savedState = savedState,
  )
}
