package com.divinelink.feature.onboarding

import com.divinelink.core.ui.UIText

object OnboardingPages {

  val initialPages = listOf(
    OnboardingPage(
      title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
      description = UIText.ResourceText(R.string.feature_onboarding_welcome_page_description),
      imageVector = null,
      showSkipButton = false,
    ),
    OnboardingPage(
      title = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_title),
      description = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_description),
      imageVector = null,
      showSkipButton = true,
      action = OnboardingAction.NavigateToTMDBLogin,
    ),
    OnboardingPage(
      title = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_title),
      description = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_description),
      imageVector = null,
      showSkipButton = true,
      action = OnboardingAction.NavigateToJellyseerrLogin,
    ),
  )

  val newFeaturePages = mapOf(
    16 to emptyList<OnboardingPage>(),
  )
}
