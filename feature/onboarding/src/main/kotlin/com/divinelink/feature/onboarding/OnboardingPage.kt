package com.divinelink.feature.onboarding

import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.ui.UIText

data class OnboardingPage(
  val title: UIText,
  val description: UIText,
  val imageVector: ImageVector?,
  val action: OnboardingAction? = null,
  val buttonText: UIText = UIText.ResourceText(R.string.feature_onboarding_next),
  val showSkipButton: Boolean = true,
)
