package com.divinelink.core.model.onboarding

import com.divinelink.core.model.UIText

data class OnboardingPage(
  val tag: String,
  val title: UIText,
  val description: UIText,
  val image: Int?,
  val action: OnboardingAction? = null,
  val showSkipButton: Boolean = true,
)
