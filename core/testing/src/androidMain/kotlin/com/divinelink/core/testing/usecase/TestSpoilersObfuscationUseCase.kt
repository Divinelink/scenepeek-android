package com.divinelink.core.testing.usecase

import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import org.junit.Rule

class TestSpoilersObfuscationUseCase {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  fun useCase(initialValue: Boolean = false) = SpoilersObfuscationUseCase(
    preferenceStorage = FakePreferenceStorage(spoilersObfuscation = initialValue),
    dispatcherProvider = mainDispatcherRule.testDispatcher,
  )
}
