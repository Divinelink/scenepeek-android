package com.andreolas.movierama.session

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

class SessionStorageTest {

  private lateinit var sessionStorage: SessionStorage

  @After
  fun tearDown() {
    sessionStorage = SessionStorage(
      storage = FakePreferenceStorage(),
      encryptedStorage = FakeEncryptedPreferenceStorage()
    )
  }

  @Test
  fun `test non null sessionId returns sessionId`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage
    )

    assertThat(sessionStorage.sessionId).isEqualTo("session")
  }

  @Test
  fun `test null sessionId returns null`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = null)

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage
    )

    assertThat(sessionStorage.sessionId).isNull()
  }

  @Test
  fun `test clearToken sets token to null`() = runTest {
    val preferenceStorage = FakePreferenceStorage(token = "token")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = FakeEncryptedPreferenceStorage()
    )

    sessionStorage.clearToken()

    assertThat(preferenceStorage.token.value).isNull()
  }

  @Test
  fun `test setToken sets token`() = runTest {
    val preferenceStorage = FakePreferenceStorage(token = "")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = FakeEncryptedPreferenceStorage()
    )

    sessionStorage.setToken("token")

    assertThat(preferenceStorage.token.value).isEqualTo("token")
  }

  @Test
  fun `test setSession sets session`() = runTest {
    val preferenceStorage = FakePreferenceStorage(hasSession = false)
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage()

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage
    )

    sessionStorage.setSession("session")

    assertThat(encryptedPreferenceStorage.sessionId).isEqualTo("session")
    assertThat(preferenceStorage.hasSession.value).isTrue()
    assertThat(preferenceStorage.token.value).isNull()
  }

  @Test
  fun `test clearSession clears session and accountId`() = runTest {
    val preferenceStorage = FakePreferenceStorage(
      hasSession = true,
      accountId = "account_id"
    )
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage
    )

    sessionStorage.clearSession()

    assertThat(encryptedPreferenceStorage.sessionId).isNull()
    assertThat(preferenceStorage.hasSession.value).isFalse()
    assertThat(preferenceStorage.token.value).isNull()
    assertThat(preferenceStorage.accountId.value).isNull()
  }

  @Test
  fun `test setAccountId sets accountId`() = runTest {
    val preferenceStorage = FakePreferenceStorage(accountId = "")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = FakeEncryptedPreferenceStorage()
    )

    sessionStorage.setAccountId("account_id")

    assertThat(preferenceStorage.accountId.value).isEqualTo("account_id")
  }
}
