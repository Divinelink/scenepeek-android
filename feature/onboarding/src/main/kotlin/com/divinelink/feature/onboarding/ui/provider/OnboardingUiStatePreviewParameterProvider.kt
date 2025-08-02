package com.divinelink.feature.onboarding.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.feature.onboarding.manager.IntroSections
import com.divinelink.feature.onboarding.ui.OnboardingUiState

@ExcludeFromKoverReport
class OnboardingUiStatePreviewParameterProvider : PreviewParameterProvider<OnboardingUiState> {
  override val values: Sequence<OnboardingUiState> = sequenceOf(
    OnboardingUiState(
      sections = IntroSections.onboardingSections,
      isFirstLaunch = true,
    ),
    OnboardingUiState(
      sections = IntroSections.v22,
      isFirstLaunch = false,
    ),
  )
}
