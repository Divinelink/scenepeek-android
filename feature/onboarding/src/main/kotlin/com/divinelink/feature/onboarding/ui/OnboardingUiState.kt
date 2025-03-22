package com.divinelink.feature.onboarding.ui

import com.divinelink.feature.onboarding.OnboardingPage

data class OnboardingUiState(
  val selectedPageIndex: Int,
  val pages: List<OnboardingPage>,
) {
  companion object {
    fun initial() = OnboardingUiState(
      selectedPageIndex = 0,
      pages = listOf(),
    )
  }
}
