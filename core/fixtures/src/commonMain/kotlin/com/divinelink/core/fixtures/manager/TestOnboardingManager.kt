package com.divinelink.core.fixtures.manager

import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.model.onboarding.IntroSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestOnboardingManager(
  showIntro: Boolean = false,
  sections: List<IntroSection> = emptyList(),
  changelog: List<IntroSection> = emptyList(),
  isInitialOnboarding: Boolean = false,
) : OnboardingManager {

  private val _showIntro = MutableStateFlow(showIntro)
  override val showIntro: Flow<Boolean> = _showIntro

  private val _sections = MutableStateFlow(sections)
  override val sections: Flow<List<IntroSection>> = _sections

  private val _isInitialOnboarding = MutableStateFlow(isInitialOnboarding)
  override val isInitialOnboarding: Flow<Boolean> = _isInitialOnboarding

  private var _changelog = MutableStateFlow(changelog)
  override val changelog: Flow<List<IntroSection>> = _changelog

  override suspend fun onOnboardingComplete() {
    _showIntro.value = false
  }

  fun setSections(pages: List<IntroSection>) {
    _sections.value = pages
  }

  fun setChangelogs(pages: List<IntroSection>) {
    _changelog.value = pages
  }

  fun setIsInitialOnboarding(isInitialOnboarding: Boolean) {
    _isInitialOnboarding.value = isInitialOnboarding
  }

  fun setShowIntro(showIntro: Boolean) {
    _showIntro.value = showIntro
  }
}
