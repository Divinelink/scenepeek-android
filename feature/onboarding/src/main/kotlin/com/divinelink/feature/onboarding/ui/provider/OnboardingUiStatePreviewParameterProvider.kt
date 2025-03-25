package com.divinelink.feature.onboarding.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.feature.onboarding.manager.OnboardingPages
import com.divinelink.feature.onboarding.ui.OnboardingUiState

@ExcludeFromKoverReport
class OnboardingUiStatePreviewParameterProvider : PreviewParameterProvider<OnboardingUiState> {
  override val values: Sequence<OnboardingUiState> = sequenceOf(
    OnboardingUiState(
      selectedPageIndex = 0,
      pages = OnboardingPages.initialPages,
      startedJobs = emptyList(),
    ),
    OnboardingUiState(
      selectedPageIndex = 1,
      pages = OnboardingPages.initialPages,
      startedJobs = emptyList(),
    ),
    OnboardingUiState(
      selectedPageIndex = 2,
      pages = OnboardingPages.initialPages,
      startedJobs = emptyList(),
    ),
  )
}
