package com.divinelink.scenepeek.session

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import kotlin.test.Test

class SessionStorageTest {

  private lateinit var sessionStorage: SessionStorage

  @After
  fun tearDown() {
    sessionStorage = SessionStorage(
      encryptedStorage = FakeEncryptedPreferenceStorage(),
    )
  }

  @Test
  fun `test non null sessionId returns sessionId`() = runTest {
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")

    sessionStorage = SessionStorage(
      encryptedStorage = encryptedPreferenceStorage,
    )

    assertThat(sessionStorage.sessionId).isEqualTo("session")
  }

  @Test
  fun `test null sessionId returns null`() = runTest {
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = null)

    sessionStorage = SessionStorage(
      encryptedStorage = encryptedPreferenceStorage,
    )

    assertThat(sessionStorage.sessionId).isNull()
  }

  @Test
  fun `test accessToken sets session and accessToken`() = runTest {
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage()

    sessionStorage = SessionStorage(
      encryptedStorage = encryptedPreferenceStorage,
    )

    sessionStorage.setAccessToken(
      sessionId = "session",
      accessToken = AccessTokenFactory.valid(),
    )

    assertThat(encryptedPreferenceStorage.sessionId).isEqualTo("session")
    assertThat(encryptedPreferenceStorage.accessToken).isEqualTo(
      AccessTokenFactory.valid().accessToken,
    )
    assertThat(encryptedPreferenceStorage.tmdbAccountId).isEqualTo(
      AccessTokenFactory.valid().accountId,
    )
  }

  @Test
  fun `test clearSession clears session and accountId and details`() = runTest {
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")

    sessionStorage = SessionStorage(
      encryptedStorage = encryptedPreferenceStorage,
    )

    sessionStorage.clearSession()

    assertThat(encryptedPreferenceStorage.sessionId).isNull()

    assertThat(sessionStorage.accountId).isNull()
  }
}
