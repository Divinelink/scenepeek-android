package com.divinelink.scenepeek.settings.app.account.usecase

import app.cash.turbine.test
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestSessionRepository
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class GetAccountDetailsUseCaseTest {

  private lateinit var repository: TestSessionRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Before
  fun setUp() {
    repository = TestSessionRepository()
  }

  @Test
  fun `given no sessionId, when getAccountDetails is called, then return failure`() = runTest {
    val sessionStorage = createSessionStorage()

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,

    )

    useCase.invoke(Unit).test {
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)
    }
  }

  @Test
  fun `given sessionId, when getAccountDetails is called, then return success`() = runTest {
    val sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockGetAccountDetails(
      Result.success(
        AccountDetailsFactory.Pinkman(),
      ),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(SessionException.Unauthenticated())::class.java,
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman())),
      )
    }
  }

  @Test
  fun `test getAccountDetails with failure and 401 clears session`() = runTest {
    val sessionStorage = createSessionStorage(
      sessionId = "session_id",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockGetAccountDetails(
      Result.failure(InvalidStatusException(401)),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(SessionException.Unauthenticated())::class.java,
      )
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(SessionException.Unauthenticated())::class.java,
      )
    }

    assertThat(sessionStorage.sessionId).isNull()
    assertThat(sessionStorage.encryptedStorage.accessToken).isNull()
    assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isNull()
  }

  @Test
  fun `test getAccountDetails with generic failure emits failure`() = runTest {
    val sessionStorage = createSessionStorage(
      sessionId = "session_id",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockGetAccountDetails(
      Result.failure(InvalidStatusException(404)),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(InvalidStatusException(404)).toString(),
      )
    }

    assertThat(sessionStorage.sessionId).isEqualTo("session_id")
    assertThat(sessionStorage.encryptedStorage.accessToken).isEqualTo(
      AccessTokenFactory.valid().accessToken,
    )
    assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isEqualTo(
      AccessTokenFactory.valid().accountId,
    )
  }

  @Test
  fun `test getAccountDetails with other failure emits failure`() = runTest {
    val sessionStorage = createSessionStorage(
      sessionId = "session_id",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockGetAccountDetails(
      Result.failure(Exception("Foo")),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(Exception("Foo")).toString(),
      )
    }

    assertThat(sessionStorage.sessionId).isEqualTo("session_id")
    assertThat(sessionStorage.encryptedStorage.accessToken).isEqualTo(
      AccessTokenFactory.valid().accessToken,
    )
    assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isEqualTo(
      AccessTokenFactory.valid().accountId,
    )
  }

  private fun createSessionStorage(
    sessionId: String? = null,
    accessToken: AccessToken? = null,
  ) = SessionStorage(
    storage = FakePreferenceStorage(),
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = sessionId,
      accessToken = accessToken?.accessToken,
      tmdbAccountId = accessToken?.accountId,
    ),
    accountStorage = FakeAccountStorage(),
  )
}
