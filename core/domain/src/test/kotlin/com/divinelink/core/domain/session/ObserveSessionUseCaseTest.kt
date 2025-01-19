package com.divinelink.core.domain.session

import app.cash.turbine.test
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class ObserveSessionUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val storage = FakePreferenceStorage()

  @Test
  fun `test on observe session correctly collects values`() = runTest {
    val useCase = ObserveSessionUseCase(
      storage = storage,
      dispatcher = testDispatcher,
    )
    useCase(Unit).test {
      assertThat(
        awaitItem(),
      ).isInstanceOf(Result.failure<Exception>(SessionException.Unauthenticated())::class.java)

      storage.setHasSession(true)

      assertThat(awaitItem()).isEqualTo(Result.success(true))
    }
  }
}
