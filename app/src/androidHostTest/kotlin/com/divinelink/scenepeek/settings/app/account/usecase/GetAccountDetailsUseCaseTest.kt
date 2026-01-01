package com.divinelink.scenepeek.settings.app.account.usecase

import app.cash.turbine.test
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.datastore.auth.observedTmdbSession
import com.divinelink.core.domain.GetAccountDetailsUseCase
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.TmdbSessionFactory
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.repository.TestSessionRepository
import com.divinelink.core.testing.storage.TestSavedStateStorage
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
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
    val storage = SessionStorageFactory.empty()

    val useCase = GetAccountDetailsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )
    authRepository.mockTMDBAccount(null)

    useCase.invoke(Unit).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()
    }
  }

  @Test
  fun `test getAccountDetails with failure and 401 clears session`() = runTest {
    val storage = SessionStorage(
      savedState = TestSavedStateStorage(
        tmdbSession = TmdbSessionFactory.full(),
      ),
    )

    repository.mockGetAccountDetails(
      Result.failure(InvalidStatusException(401)),
    )
    authRepository.mockTMDBAccount(null)

    val useCase = GetAccountDetailsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem().toString() shouldBe Result.failure<Exception>(
        InvalidStatusException(401),
      ).toString()
    }

    authRepository.verifyClearTMDBSessionInvoked()
  }

  @Test
  fun `test getAccountDetails with generic failure emits failure`() = runTest {
    val storage = SessionStorageFactory.full()

    repository.mockGetAccountDetails(
      Result.failure(InvalidStatusException(404)),
    )
    authRepository.mockTMDBAccount(null)

    val useCase = GetAccountDetailsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem().toString() shouldBe Result.failure<Exception>(
        InvalidStatusException(404),
      ).toString()
    }

    storage.savedState.observedTmdbSession.first() shouldBe TmdbSessionFactory.full()
    authRepository.verifyClearTMDBSessionInvoked()
  }

  @Test
  fun `test getAccountDetails with other failure emits failure and clears data`() = runTest {
    val storage = SessionStorageFactory.full()

    repository.mockGetAccountDetails(
      Result.failure(Exception("Foo")),
    )
    authRepository.mockTMDBAccount(null)

    val useCase = GetAccountDetailsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem().toString() shouldBe Result.failure<Exception>(
        Exception("Foo"),
      ).toString()
    }

    storage.savedState.observedTmdbSession.first() shouldBe TmdbSessionFactory.full()
    authRepository.verifyClearTMDBSessionInvoked()
  }

  @Test
  fun `test getAccountDetails with unauthorised clears session`() = runTest {
    val storage = SessionStorageFactory.full()

    authRepository.mockTMDBAccount(null)
    repository.mockGetAccountDetails(
      Result.failure(AppException.Unauthorized("Unauthorized")),
    )

    val useCase = GetAccountDetailsUseCase(
      storage = storage,
      repository = repository.mock,
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )

    useCase.invoke(Unit).test {
      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      storage.savedState.observedTmdbSession.first() shouldBe TmdbSessionFactory.full()
      authRepository.verifyClearTMDBSessionInvoked(times = 2)
    }
  }

  @Test
  fun `test getAccountDetails from local storage with unknown remote error`() = runTest {
    val sessionStorage = SessionStorageFactory.full()

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
      awaitItem() shouldBe Result.success(TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()))

      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem().toString() shouldBe Result.failure<Exception>(
        AppException.Unknown(),
      ).toString()

      sessionStorage.sessionId shouldBe "sessionId"
    }
  }

  @Test
  fun `test getAccountDetails from local storage with unauthorised error clears data`() = runTest {
    authRepository.mockTMDBAccount(AccountDetailsFactory.Pinkman())
    val sessionStorage = SessionStorageFactory.full()

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
      awaitItem() shouldBe Result.success(TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()))

      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem().toString() shouldBe Result.failure<Exception>(
        SessionException.Unauthenticated(),
      ).toString()

      authRepository.verifyClearTMDBSessionInvoked()
    }
  }
}
