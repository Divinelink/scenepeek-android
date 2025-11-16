package com.divinelink.core.domain.session

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestAuthRepository
import com.google.common.truth.Truth.assertThat
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
      dispatcher = testDispatcher,
    )
    authRepository.mockTMDBAccount(
      flowOf(
        null,
        AccountDetailsFactory.Pinkman(),
      ),
    )

    useCase(Unit).test {
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)

      assertThat(awaitItem()).isEqualTo(Result.success(true))

      awaitComplete()
    }
  }

  @Test
  fun `test on observe account emits unauthenticated when account id becomes null`() = runTest {
    val useCase = ObserveAccountUseCase(
      authRepository = authRepository.mock,
      dispatcher = testDispatcher,
    )
    authRepository.mockTMDBAccount(
      flowOf(
        AccountDetailsFactory.Pinkman(),
        null,
      ),
    )

    useCase(Unit).test {
      assertThat(awaitItem()).isEqualTo(Result.success(true))

      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)

      awaitComplete()
    }
  }
}
