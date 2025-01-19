package com.divinelink.scenepeek.session

import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import kotlin.test.Test

class SessionStorageTest {

  private lateinit var sessionStorage: SessionStorage

  @After
  fun tearDown() {
    sessionStorage = SessionStorage(
      storage = FakePreferenceStorage(),
      encryptedStorage = FakeEncryptedPreferenceStorage(),
    )
  }

  @Test
  fun `test non null sessionId returns sessionId`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
    )

    assertThat(sessionStorage.sessionId).isEqualTo("session")
  }

  @Test
  fun `test null sessionId returns null`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = null)

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
    )

    assertThat(sessionStorage.sessionId).isNull()
  }

  @Test
  fun `test setSession sets session`() = runTest {
    val preferenceStorage = FakePreferenceStorage(hasSession = false)
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage()

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
    )

    sessionStorage.setSession("session")

    assertThat(encryptedPreferenceStorage.sessionId).isEqualTo("session")
    assertThat(preferenceStorage.hasSession.value).isTrue()
  }

  @Test
  fun `test clearSession clears session and accountId`() = runTest {
    val preferenceStorage = FakePreferenceStorage(
      hasSession = true,
      accountId = "account_id",
    )
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
    )

    sessionStorage.clearSession()

    assertThat(encryptedPreferenceStorage.sessionId).isNull()
    assertThat(preferenceStorage.hasSession.value).isFalse()
    assertThat(preferenceStorage.accountId.value).isNull()
  }

  @Test
  fun `test setAccountId sets accountId`() = runTest {
    val preferenceStorage = FakePreferenceStorage(accountId = "")

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = FakeEncryptedPreferenceStorage(),
    )

    sessionStorage.setAccountId("account_id")

    assertThat(preferenceStorage.accountId.value).isEqualTo("account_id")
  }

  @Test
  fun `test clearJellyseerrSession clears jellyseerr data`() = runTest {
    val preferenceStorage = FakePreferenceStorage(
      jellyseerrAccount = "Zabaob",
      jellyseerrAddress = "http://localhost:5050",
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(
      jellyseerrAuthCookie = "123456789qwertyuiop",
      jellyseerrPassword = "password",
    )

    val sessionStorage = SessionStorage(
      preferenceStorage,
      encryptedPreferenceStorage,
    )

    assertThat(preferenceStorage.jellyseerrAccount.first()).isEqualTo("Zabaob")
    assertThat(preferenceStorage.jellyseerrAddress.first()).isEqualTo("http://localhost:5050")
    assertThat(preferenceStorage.jellyseerrAuthMethod.first()).isEqualTo(
      JellyseerrAuthMethod.JELLYSEERR.name,
    )
    assertThat(encryptedPreferenceStorage.jellyseerrAuthCookie).isEqualTo("123456789qwertyuiop")

    sessionStorage.clearJellyseerrSession()

    assertThat(preferenceStorage.jellyseerrAccount.first()).isNull()
    assertThat(preferenceStorage.jellyseerrAddress.first()).isNull()
    assertThat(preferenceStorage.jellyseerrAuthMethod.first()).isNull()
    assertThat(encryptedPreferenceStorage.jellyseerrAuthCookie).isNull()
    assertThat(encryptedPreferenceStorage.jellyseerrPassword).isNull()
  }
}
