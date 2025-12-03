package com.divinelink.feature.onboarding.ui.provider

import com.divinelink.core.commons.ExcludeFromKoverReport
import com.divinelink.feature.onboarding.manager.IntroSections
import com.divinelink.feature.onboarding.ui.OnboardingUiState
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@ExcludeFromKoverReport
class OnboardingUiStatePreviewParameterProvider : PreviewParameterProvider<OnboardingUiState> {
  override val values: Sequence<OnboardingUiState> = sequenceOf(
    OnboardingUiState(
      sections = IntroSections.onboardingSections,
      isFirstLaunch = true,
    ),
    OnboardingUiState(
      sections = IntroSections.v23,
      isFirstLaunch = false,
    ),
  )
}
