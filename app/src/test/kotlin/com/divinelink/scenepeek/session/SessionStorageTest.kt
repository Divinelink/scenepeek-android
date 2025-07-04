package com.divinelink.scenepeek.session

import app.cash.turbine.test
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.testing.storage.FakeAccountStorage
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
      accountStorage = FakeAccountStorage(),
    )
  }

  @Test
  fun `test non null sessionId returns sessionId`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")
    val accountStorage = FakeAccountStorage()

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = accountStorage,
    )

    assertThat(sessionStorage.sessionId).isEqualTo("session")
  }

  @Test
  fun `test null sessionId returns null`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = null)
    val accountStorage = FakeAccountStorage()

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = accountStorage,
    )

    assertThat(sessionStorage.sessionId).isNull()
  }

  @Test
  fun `test accessToken sets session and accessToken`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage()

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = FakeAccountStorage(),
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
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")
    val accountStorage = FakeAccountStorage(
      accountDetails = AccountDetailsFactory.Pinkman(),
    )

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = accountStorage,
    )

    sessionStorage.clearSession()

    assertThat(encryptedPreferenceStorage.sessionId).isNull()

    assertThat(sessionStorage.accountId).isNull()
    sessionStorage.accountStorage.accountDetails.test {
      assertThat(awaitItem()).isNull()
    }
  }

  @Test
  fun `test setAccountDetails does not sets accountId`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val accountStorage = FakeAccountStorage()

    sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = FakeEncryptedPreferenceStorage(),
      accountStorage = accountStorage,
    )

    sessionStorage.accountStorage.accountId.test {
      assertThat(awaitItem()).isNull()
    }

    sessionStorage.accountStorage.accountDetails.test {
      assertThat(awaitItem()).isNull()
    }

    sessionStorage.setTMDbAccountDetails(AccountDetailsFactory.Pinkman())

    assertThat(sessionStorage.accountId).isNotEqualTo(
      AccountDetailsFactory.Pinkman().id.toString(),
    )

    sessionStorage.accountStorage.accountDetails.test {
      assertThat(awaitItem()).isEqualTo(AccountDetailsFactory.Pinkman())
    }
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
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = FakeAccountStorage(),
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

  @Test
  fun `test setJellyseerrSession`() = runTest {
    val preferenceStorage = FakePreferenceStorage()
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage()

    val sessionStorage = SessionStorage(
      storage = preferenceStorage,
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = FakeAccountStorage(),
    )

    sessionStorage.setJellyseerrSession(
      username = "Zabaob",
      address = "http://localhost:5050",
      authMethod = JellyseerrAuthMethod.JELLYSEERR.name,
      password = "password",
    )

    assertThat(preferenceStorage.jellyseerrAccount.first()).isEqualTo("Zabaob")
    assertThat(preferenceStorage.jellyseerrAddress.first()).isEqualTo("http://localhost:5050")
    assertThat(preferenceStorage.jellyseerrAuthMethod.first()).isEqualTo(
      JellyseerrAuthMethod.JELLYSEERR.name,
    )

    sessionStorage.clearJellyseerrSession()

    assertThat(preferenceStorage.jellyseerrAccount.first()).isNull()
    assertThat(preferenceStorage.jellyseerrAddress.first()).isNull()
    assertThat(preferenceStorage.jellyseerrAuthMethod.first()).isNull()
    assertThat(encryptedPreferenceStorage.jellyseerrPassword).isNull()
  }
}
