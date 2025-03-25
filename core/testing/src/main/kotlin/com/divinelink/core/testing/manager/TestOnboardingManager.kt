package com.divinelink.core.testing.manager

import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.onboarding.OnboardingPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestOnboardingManager(
  shouldShowOnboarding: Boolean = false,
  onboardingPages: List<OnboardingPage> = emptyList(),
  isInitialOnboarding: Boolean = false,
) : OnboardingManager {

  private val _shouldShowOnboarding = MutableStateFlow(shouldShowOnboarding)
  override val shouldShowOnboarding: Flow<Boolean> = _shouldShowOnboarding

  private val _onboardingPages = MutableStateFlow(onboardingPages)
  override val onboardingPages: Flow<List<OnboardingPage>> = _onboardingPages

  private val _isInitialOnboarding = MutableStateFlow(isInitialOnboarding)
  override val isInitialOnboarding: Flow<Boolean> = _isInitialOnboarding

  override suspend fun onOnboardingComplete() {
    _shouldShowOnboarding.value = false
  }

  fun setOnboardingPages(pages: List<OnboardingPage>) {
    _onboardingPages.value = pages
  }

  fun setShouldShowOnboarding(shouldShowOnboarding: Boolean) {
    _shouldShowOnboarding.value = shouldShowOnboarding
  }

  fun setIsInitialOnboarding(isInitialOnboarding: Boolean) {
    _isInitialOnboarding.value = isInitialOnboarding
  }
}
