package com.divinelink.feature.onboarding

import com.divinelink.core.model.UIText

data class OnboardingPage(
  val tag: String,
  val title: UIText,
  val description: UIText,
  @DrawableRes val image: Int?,
  val action: OnboardingAction? = null,
  val buttonText: UIText = UIText.ResourceText(R.string.feature_onboarding_next),
  val showSkipButton: Boolean = true,
)
