package com.divinelink.core.domain.onboarding

import com.divinelink.core.model.onboarding.OnboardingPage
import kotlinx.coroutines.flow.Flow

interface OnboardingManager {
  val shouldShowOnboarding: Flow<Boolean>
  val onboardingPages: Flow<List<OnboardingPage>>
  val isInitialOnboarding: Flow<Boolean>
  suspend fun onOnboardingComplete()
}
