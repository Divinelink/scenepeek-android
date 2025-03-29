package com.divinelink.scenepeek.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.onboarding.ui.OnboardingContentPreview
import com.divinelink.feature.onboarding.ui.OnboardingUiState
import com.divinelink.feature.onboarding.ui.provider.OnboardingUiStatePreviewParameterProvider

@Previews
@Composable
fun OnboardingContentScreenshots(
  @PreviewParameter(OnboardingUiStatePreviewParameterProvider::class) uiState: OnboardingUiState,
) {
  OnboardingContentPreview(uiState)
}
