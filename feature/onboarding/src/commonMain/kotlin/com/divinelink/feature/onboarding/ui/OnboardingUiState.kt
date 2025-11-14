package com.divinelink.feature.onboarding.ui

import com.divinelink.core.model.onboarding.IntroSection

data class OnboardingUiState(
  val sections: List<IntroSection>,
  val isFirstLaunch: Boolean,
) {
  companion object {
    fun initial() = OnboardingUiState(
      sections = emptyList(),
      isFirstLaunch = false,
    )
  }
}
