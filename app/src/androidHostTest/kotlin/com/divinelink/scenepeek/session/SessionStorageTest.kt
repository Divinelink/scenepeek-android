package com.divinelink.scenepeek.session

import app.cash.turbine.test
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.fixtures.model.session.TmdbSessionFactory
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.storage.TestSavedStateStorage
import com.google.common.truth.Truth.assertThat
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.After
import kotlin.test.Test

class SessionStorageTest {

  private lateinit var sessionStorage: SessionStorage

  @After
  fun tearDown() {
    sessionStorage = SessionStorageFactory.empty()
  }

  @Test
  fun `test non null sessionId returns sessionId`() = runTest {
    sessionStorage = SessionStorageFactory.full()

    assertThat(sessionStorage.sessionId).isEqualTo("sessionId")
  }

  @Test
  fun `test null sessionId returns null`() = runTest {
    sessionStorage = SessionStorageFactory.empty()

    assertThat(sessionStorage.sessionId).isNull()
  }

  @Test
  fun `test accessToken sets session and accessToken`() = runTest {
    val savedState = TestSavedStateStorage()

    sessionStorage = SessionStorage(
      savedState = savedState,
    )

    sessionStorage.setAccessToken(
      sessionId = "session",
      accessToken = AccessTokenFactory.valid(),
    )

    savedState.savedState.test {
      awaitItem().tmdbSession shouldBe TmdbSessionFactory.full().copy(
        sessionId = "session",
      )
    }
  }
}
