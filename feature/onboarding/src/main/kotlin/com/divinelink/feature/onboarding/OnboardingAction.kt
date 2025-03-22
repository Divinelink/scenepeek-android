package com.divinelink.feature.onboarding

import com.divinelink.core.ui.UIText

sealed class OnboardingAction(
  val actionText: UIText,
  val completedActionText: UIText,
  open val isComplete: Boolean = false,
) {
  data class NavigateToTMDBLogin(override val isComplete: Boolean) :
    OnboardingAction(
      actionText = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_action),
      completedActionText = UIText.ResourceText(R.string.feature_onboarding_successfully_connected),
      isComplete = isComplete,
    )

  data class NavigateToJellyseerrLogin(override val isComplete: Boolean) :
    OnboardingAction(
      actionText = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_action),
      completedActionText = UIText.ResourceText(R.string.feature_onboarding_successfully_connected),
    )
}
