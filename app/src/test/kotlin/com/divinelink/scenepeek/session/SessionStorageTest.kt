package com.divinelink.scenepeek.session

import app.cash.turbine.test
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.testing.storage.FakeAccountStorage
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
      accountStorage = FakeAccountStorage(),
    )
  }

  @Test
  fun `test non null sessionId returns sessionId`() = runTest {
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")
    val accountStorage = FakeAccountStorage()

    sessionStorage = SessionStorage(
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = accountStorage,
    )

    assertThat(sessionStorage.sessionId).isEqualTo("session")
  }

  @Test
  fun `test null sessionId returns null`() = runTest {
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = null)
    val accountStorage = FakeAccountStorage()

    sessionStorage = SessionStorage(
      encryptedStorage = encryptedPreferenceStorage,
      accountStorage = accountStorage,
    )

    assertThat(sessionStorage.sessionId).isNull()
  }

  @Test
  fun `test accessToken sets session and accessToken`() = runTest {
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage()

    sessionStorage = SessionStorage(
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
    val encryptedPreferenceStorage = FakeEncryptedPreferenceStorage(sessionId = "session")
    val accountStorage = FakeAccountStorage(
      accountDetails = AccountDetailsFactory.Pinkman(),
    )

    sessionStorage = SessionStorage(
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
    val accountStorage = FakeAccountStorage()

    sessionStorage = SessionStorage(
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
}
