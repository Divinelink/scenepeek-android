package com.divinelink.core.domain.session

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.session.TmdbSessionFactory
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.storage.SessionStorageFactory
import com.divinelink.core.testing.repository.TestAuthRepository
import com.divinelink.core.testing.storage.TestSavedStateStorage
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class ObserveAccountUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val authRepository = TestAuthRepository()

  @Test
  fun `test on observe account correctly collects values`() = runTest {
    val useCase = ObserveAccountUseCase(
      authRepository = authRepository.mock,
      sessionStorage = SessionStorageFactory.full(),
      dispatcher = testDispatcher,
    )
    authRepository.mockTMDBAccount(
      flowOf(
        null,
        AccountDetailsFactory.Pinkman(),
      ),
    )

    useCase(Unit).test {
      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      awaitItem() shouldBe Result.success(
        TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()),
      )
    }
  }

  @Test
  fun `test on observe account emits unauthenticated when account id becomes null`() = runTest {
    val savedState = TestSavedStateStorage()
    val sessionStorage = SessionStorageFactory.empty(
      savedState = savedState,
    )

    val useCase = ObserveAccountUseCase(
      authRepository = authRepository.mock,
      sessionStorage = sessionStorage,
      dispatcher = testDispatcher,
    )

    val channel = Channel<AccountDetails?>()
    authRepository.mockTMDBAccount(channel)

    useCase(Unit).test {
      channel.send(null)
      awaitItem() shouldBe Result.success(TMDBAccount.Anonymous)

      savedState.setTMDBSession(TmdbSessionFactory.full())
      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      channel.send(AccountDetailsFactory.Pinkman())
      awaitItem() shouldBe Result.success(
        TMDBAccount.LoggedIn(AccountDetailsFactory.Pinkman()),
      )

      channel.send(null)
      awaitItem() shouldBe Result.success(TMDBAccount.Loading)

      savedState.clearTMDBSession()
      awaitItem() shouldBe Result.success(TMDBAccount.Anonymous)
    }
  }
}
