package com.divinelink.feature.onboarding.ui

import com.divinelink.core.model.onboarding.OnboardingPage

data class OnboardingUiState(
  val selectedPageIndex: Int,
  val pages: List<OnboardingPage>,
  val startedJobs: List<String>,
) {
  companion object {
    fun initial() = OnboardingUiState(
      selectedPageIndex = 0,
      pages = listOf(),
      startedJobs = listOf(),
    )
  }
}
