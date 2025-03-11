package com.divinelink.feature.onboarding.ui

import com.divinelink.feature.onboarding.OnboardingPage
import com.divinelink.feature.onboarding.OnboardingPages

data class OnboardingUiState(val pages: List<OnboardingPage>) {
  companion object {
    fun initial() = OnboardingUiState(
      pages = OnboardingPages.initialPages,
    )
  }
}
