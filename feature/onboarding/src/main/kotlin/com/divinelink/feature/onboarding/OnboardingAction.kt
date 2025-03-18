package com.divinelink.feature.onboarding

import com.divinelink.core.ui.UIText

sealed class OnboardingAction(
  val actionText: UIText,
  val completedActionText: UIText,
  val isComplete: Boolean = false,
) {
  data object NavigateToTMDBLogin : OnboardingAction(
    actionText = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_action),
    completedActionText = UIText.ResourceText(R.string.feature_onboarding_successfully_connected),
  )

  data object NavigateToJellyseerrLogin : OnboardingAction(
    actionText = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_action),
    completedActionText = UIText.ResourceText(R.string.feature_onboarding_successfully_connected),
  )
}
