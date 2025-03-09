package com.divinelink.feature.onboarding

import com.divinelink.core.ui.UIText

sealed class OnboardingAction(val actionText: UIText?) {
  data object NavigateToTMDBLogin : OnboardingAction(
    UIText.ResourceText(R.string.feature_onboarding_tmdb_page_action),
  )

  data object NavigateToJellyseerrLogin : OnboardingAction(
    UIText.ResourceText(R.string.feature_onboarding_tmdb_page_action),
  )

  data object CompleteOnboarding : OnboardingAction(null)
}
