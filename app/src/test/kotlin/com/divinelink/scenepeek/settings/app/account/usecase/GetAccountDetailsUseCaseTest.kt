package com.divinelink.scenepeek.settings.app.account.usecase

import app.cash.turbine.test
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.AccessTokenFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.session.AccessToken
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestSessionRepository
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class GetAccountDetailsUseCaseTest {

  private lateinit var repository: TestSessionRepository
  private lateinit var authRepository: TestAuthRepository

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  @Before
  fun setUp() {
    repository = TestSessionRepository()
    authRepository = TestAuthRepository()
  }

  @Test
  fun `given no sessionId, when getAccountDetails is called, then return failure`() = runTest {
    val sessionStorage = createSessionStorage()

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )
    authRepository.mockTMDBAccount(null)

    useCase.invoke(Unit).test {
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)

      awaitComplete()
    }
  }

  @Test
  fun `given no account id when getAccountDetails is called return unauthenticated`() = runTest {
    val sessionStorage = createSessionStorage(sessionId = "session_id")

    repository.mockGetAccountDetails(
      Result.success(
        AccountDetailsFactory.Pinkman(),
      ),
    )
    authRepository.mockTMDBAccount(null)

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(SessionException.Unauthenticated())::class.java,
      )
      awaitComplete()
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
    authRepository.mockTMDBAccount(null)

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(SessionException.Unauthenticated())::class.java,
      )
      assertThat(awaitItem()).isInstanceOf(
        Result.failure<Exception>(SessionException.Unauthenticated())::class.java,
      )

      awaitComplete()
    }

    assertThat(sessionStorage.sessionId).isNull()
    assertThat(sessionStorage.encryptedStorage.accessToken).isNull()
    assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isNull()

    authRepository.verifyClearTMDBAccountInvoked()
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
    authRepository.mockTMDBAccount(null)

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(InvalidStatusException(404)).toString(),
      )

      awaitComplete()
    }

    assertThat(sessionStorage.sessionId).isNull()
    assertThat(sessionStorage.encryptedStorage.accessToken).isNull()
    assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isNull()
  }

  @Test
  fun `test getAccountDetails with other failure emits failure and clears data`() = runTest {
    val sessionStorage = createSessionStorage(
      sessionId = "session_id",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockGetAccountDetails(
      Result.failure(Exception("Foo")),
    )
    authRepository.mockTMDBAccount(null)

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(Exception("Foo")).toString(),
      )

      awaitComplete()
    }

    assertThat(sessionStorage.sessionId).isNull()
    assertThat(sessionStorage.encryptedStorage.accessToken).isNull()
    assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isNull()
    authRepository.verifyClearTMDBAccountInvoked()
  }

  @Test
  fun `test getAccountDetails with unauthorised clears session`() = runTest {
    val sessionStorage = createSessionStorage(
      sessionId = "session_id",
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(null)
    repository.mockGetAccountDetails(
      Result.failure(AppException.Unauthorized("Unauthorized")),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )

      assertThat(sessionStorage.sessionId).isNull()
      assertThat(sessionStorage.encryptedStorage.accessToken).isNull()
      assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isNull()
      authRepository.verifyClearTMDBAccountInvoked(times = 2)

      awaitComplete()
    }
  }

  @Test
  fun `test getAccountDetails from local storage with unknown remote error`() = runTest {
    val sessionStorage = createSessionStorage(
      sessionId = "session_id",
      accessToken = AccessTokenFactory.valid(),
    )

    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    repository.mockGetAccountDetails(
      Result.failure(AppException.Unknown()),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman())),
      )
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(AppException.Unknown()).toString(),
      )

      assertThat(sessionStorage.sessionId).isEqualTo("session_id")

      awaitComplete()
    }
  }

  @Test
  fun `test getAccountDetails from local storage with unauthorised error clears data`() = runTest {
    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    val sessionStorage = createSessionStorage(
      sessionId = "session_id",
      accessToken = AccessTokenFactory.valid(),
    )

    repository.mockGetAccountDetails(
      Result.failure(AppException.Unauthorized("Unauthorized")),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = sessionStorage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman())),
      )
      assertThat(awaitItem().toString()).isEqualTo(
        Result.failure<Exception>(SessionException.Unauthenticated()).toString(),
      )
      awaitComplete()

      assertThat(sessionStorage.sessionId).isNull()
      assertThat(sessionStorage.encryptedStorage.accessToken).isNull()
      assertThat(sessionStorage.encryptedStorage.tmdbAccountId).isNull()
      authRepository.verifyClearTMDBAccountInvoked()
    }
  }

  private fun createSessionStorage(
    sessionId: String? = null,
    accessToken: AccessToken? = null,
  ) = SessionStorage(
    encryptedStorage = FakeEncryptedPreferenceStorage(
      sessionId = sessionId,
      accessToken = accessToken?.accessToken,
      tmdbAccountId = accessToken?.accountId,
    ),
  )
}
