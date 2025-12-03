package com.divinelink.core.domain.onboarding

import com.divinelink.core.model.onboarding.IntroSection
import kotlinx.coroutines.flow.Flow

interface OnboardingManager {
  val showIntro: Flow<Boolean>
  val sections: Flow<List<IntroSection>>
  val isInitialOnboarding: Flow<Boolean>
  suspend fun onOnboardingComplete()
}
