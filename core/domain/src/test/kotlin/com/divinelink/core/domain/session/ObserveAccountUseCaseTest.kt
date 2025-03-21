package com.divinelink.core.domain.session

import app.cash.turbine.test
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakeAccountStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class ObserveAccountUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val storage = FakeAccountStorage()

  @Test
  fun `test on observe account correctly collects values`() = runTest {
    val useCase = ObserveAccountUseCase(
      storage = storage,
      dispatcher = testDispatcher,
    )
    useCase(Unit).test {
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)

      storage.setAccountDetails(AccountDetailsFactory.Pinkman())

      assertThat(awaitItem()).isEqualTo(Result.success(true))
    }
  }

  @Test
  fun `test on observe account emits unauthenticated when account id becomes null`() = runTest {
    val useCase = ObserveAccountUseCase(
      storage = storage,
      dispatcher = testDispatcher,
    )
    storage.setAccountDetails(AccountDetailsFactory.Pinkman())

    useCase(Unit).test {
      assertThat(awaitItem()).isEqualTo(Result.success(true))

      storage.clearAccountDetails()
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)
    }
  }
}
