package com.divinelink.feature.onboarding

import com.divinelink.core.ui.UIText

object OnboardingPages {

  val jellyseerrPage = OnboardingPage(
    tag = "jellyseerr",
    title = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_description),
    image = com.divinelink.core.ui.R.drawable.core_ui_ic_jellyseerr,
    showSkipButton = false,
    action = OnboardingAction.NavigateToJellyseerrLogin(isComplete = false),
  )

  val tmdbPage = OnboardingPage(
    tag = "tmdb",
    title = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_description),
    image = com.divinelink.core.model.R.drawable.core_model_ic_tmdb,
    showSkipButton = true,
    action = OnboardingAction.NavigateToTMDBLogin(isComplete = false),
  )

  val initialPages = listOf(
    OnboardingPage(
      tag = "welcome",
      title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
      description = UIText.ResourceText(R.string.feature_onboarding_welcome_page_description),
      image = null,
      showSkipButton = true,
    ),
    tmdbPage,
    jellyseerrPage,
  )

  val newFeaturePages = mapOf(
    16 to emptyList<OnboardingPage>(),
  )
}
