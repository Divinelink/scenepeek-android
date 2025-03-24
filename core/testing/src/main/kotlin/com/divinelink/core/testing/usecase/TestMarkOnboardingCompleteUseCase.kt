package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.onboarding.MarkOnboardingCompleteUseCase
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.TestOnboardingStorage
import org.junit.Rule

class TestMarkOnboardingCompleteUseCase {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  val mock: MarkOnboardingCompleteUseCase = MarkOnboardingCompleteUseCase(
    storage = TestOnboardingStorage(),
    dispatcher = testDispatcher,
  )
}
