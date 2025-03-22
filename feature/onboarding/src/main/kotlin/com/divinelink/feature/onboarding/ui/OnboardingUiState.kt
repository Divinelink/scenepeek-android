package com.divinelink.feature.onboarding.ui

import com.divinelink.feature.onboarding.OnboardingPage

data class OnboardingUiState(val pages: List<OnboardingPage>) {
  companion object {
    fun initial() = OnboardingUiState(
      pages = listOf(),
    )
  }
}
