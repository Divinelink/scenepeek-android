package com.divinelink.core.domain.session

import app.cash.turbine.test
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.session.RequestToken
import com.divinelink.core.model.session.Session
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestSessionRepository
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class CreateSessionUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  val repository = TestSessionRepository()

  private val storage = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(),
    accountStorage = FakeAccountStorage(),
  )

  @Test
  fun `test createSession with null request token clears session and token`() = runTest {
    repository.mockRetrieveRequestToken(Result.failure(SessionException.RequestTokenNotFound()))

    storage.setAccessToken(
      sessionId = "sessionId",
      accessToken = AccessTokenFactory.valid(),
    )

    CreateSessionUseCase(
      repository = repository.mock,
      storage = storage,
      dispatcher = testDispatcher,
    ).invoke(Unit)

    assertThat(storage.sessionId).isEqualTo(null)
    assertThat(storage.accountId).isEqualTo(null)
    assertThat(storage.encryptedStorage.accessToken).isEqualTo(null)
  }

  @Test
  fun `test createSession with valid request token and failure accessToken`() = runTest {
    repository.mockRetrieveRequestToken(Result.success(RequestToken("123456789")))
    repository.mockCreateAccessToken(Result.failure(Exception("Access token creation failed")))

    storage.setAccessToken(
      sessionId = "sessionId",
      accessToken = AccessTokenFactory.valid(),
    )

    CreateSessionUseCase(
      repository = repository.mock,
      storage = storage,
      dispatcher = testDispatcher,
    ).invoke(Unit)

    assertThat(storage.sessionId).isEqualTo(null)
    assertThat(storage.accountId).isEqualTo(null)
    assertThat(storage.encryptedStorage.accessToken).isEqualTo(null)
  }

  @Test
  fun `test createSession with success accessToken and createSession`() = runTest {
    repository.mockRetrieveRequestToken(Result.success(RequestToken("123456789")))
    repository.mockCreateAccessToken(Result.success(AccessTokenFactory.valid()))
    repository.mockCreateSession(Result.success(Session(id = "sessionId")))

    CreateSessionUseCase(
      repository = repository.mock,
      storage = storage,
      dispatcher = testDispatcher,
    ).invoke(Unit)

    assertThat(storage.sessionId).isEqualTo("sessionId")
    assertThat(storage.accountId).isEqualTo(AccessTokenFactory.valid().accountId)
    assertThat(storage.encryptedStorage.accessToken).isEqualTo(
      AccessTokenFactory.valid().accessToken,
    )

    repository.clearRequestTokenInvoke()
  }

  @Test
  fun `test createSession with success and accountDetails`() = runTest {
    repository.mockRetrieveRequestToken(Result.success(RequestToken("123456789")))
    repository.mockCreateAccessToken(Result.success(AccessTokenFactory.valid()))
    repository.mockCreateSession(Result.success(Session(id = "sessionId")))
    repository.mockGetAccountDetails(Result.success(AccountDetailsFactory.Pinkman()))

    CreateSessionUseCase(
      repository = repository.mock,
      storage = storage,
      dispatcher = testDispatcher,
    ).invoke(Unit)

    assertThat(storage.sessionId).isEqualTo("sessionId")
    assertThat(storage.accountId).isEqualTo(AccessTokenFactory.valid().accountId)
    assertThat(storage.encryptedStorage.accessToken).isEqualTo(
      AccessTokenFactory.valid().accessToken,
    )
    storage.accountStorage.accountDetails.test {
      assertThat(awaitItem()).isEqualTo(AccountDetailsFactory.Pinkman())
    }

    repository.clearRequestTokenInvoke()
  }

  @Test
  fun `test createSession with createSession failure`() = runTest {
    repository.mockRetrieveRequestToken(Result.success(RequestToken("123456789")))
    repository.mockCreateAccessToken(Result.success(AccessTokenFactory.valid()))
    repository.mockCreateSession(Result.failure(Exception("Session creation failed")))
    repository.mockGetAccountDetails(Result.success(AccountDetailsFactory.Pinkman()))

    storage.setAccessToken(
      sessionId = "sessionId",
      accessToken = AccessTokenFactory.valid(),
    )

    CreateSessionUseCase(
      repository = repository.mock,
      storage = storage,
      dispatcher = testDispatcher,
    ).invoke(Unit)

    assertThat(storage.sessionId).isNull()
    assertThat(storage.accountId).isNull()
    assertThat(storage.encryptedStorage.accessToken).isNull()
    storage.accountStorage.accountDetails.test {
      assertThat(awaitItem()).isNull()
    }

    repository.clearRequestTokenInvoke()
  }
}
