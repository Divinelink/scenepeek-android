package com.divinelink.core.domain.credits

import app.cash.turbine.test
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class SpoilersObfuscationUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test spoilers obfuscation with initially hidden spoilers`() = runTest {
    val useCase = SpoilersObfuscationUseCase(
      preferenceStorage = FakePreferenceStorage(
        spoilersObfuscation = false,
      ),
      dispatcherProvider = mainDispatcherRule.testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isEqualTo(Result.success(false))
      useCase.setSpoilersObfuscation(true)
      assertThat(awaitItem()).isEqualTo(Result.success(true))
    }
  }

  @Test
  fun `test spoilers obfuscation with initially shown spoilers`() = runTest {
    val useCase = SpoilersObfuscationUseCase(
      preferenceStorage = FakePreferenceStorage(
        spoilersObfuscation = true,
      ),
      dispatcherProvider = mainDispatcherRule.testDispatcher,
    )

    useCase.invoke(Unit).test {
      assertThat(awaitItem()).isEqualTo(Result.success(true))
      useCase.setSpoilersObfuscation(false)
      assertThat(awaitItem()).isEqualTo(Result.success(false))
    }
  }
}
