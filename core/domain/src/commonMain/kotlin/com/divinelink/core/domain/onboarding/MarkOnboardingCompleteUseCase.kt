package com.divinelink.core.domain.onboarding

import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.domain.UseCase
import com.divinelink.core.datastore.onboarding.OnboardingStorage

class MarkOnboardingCompleteUseCase(
  private val storage: OnboardingStorage,
  val dispatcher: DispatcherProvider,
) : UseCase<Unit, Unit>(dispatcher.default) {

  override suspend fun execute(parameters: Unit) {
    storage.setOnboardingCompleted()
  }
}
