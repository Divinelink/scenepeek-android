package com.divinelink.core.fixtures.manager

import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.onboarding.IntroSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestOnboardingManager(
  showIntro: Boolean = false,
  pages: List<IntroSection> = emptyList(),
  isInitialOnboarding: Boolean = false,
) : OnboardingManager {

  private val _showIntro = MutableStateFlow(showIntro)
  override val showIntro: Flow<Boolean> = _showIntro

  private val _introPages = MutableStateFlow(pages)
  override val sections: Flow<List<IntroSection>> = _introPages

  private val _isInitialOnboarding = MutableStateFlow(isInitialOnboarding)
  override val isInitialOnboarding: Flow<Boolean> = _isInitialOnboarding

  override suspend fun onOnboardingComplete() {
    _showIntro.value = false
  }

  fun setIntroPages(pages: List<IntroSection>) {
    _introPages.value = pages
  }

  fun setIsInitialOnboarding(isInitialOnboarding: Boolean) {
    _isInitialOnboarding.value = isInitialOnboarding
  }
}
