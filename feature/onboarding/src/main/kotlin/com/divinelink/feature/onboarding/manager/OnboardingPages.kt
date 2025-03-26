package com.divinelink.feature.onboarding.manager

import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.model.onboarding.OnboardingPage
import com.divinelink.feature.onboarding.R

object OnboardingPages {

  val jellyseerrPage = OnboardingPage(
    tag = "jellyseerr",
    title = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_description),
    image = com.divinelink.core.ui.R.drawable.core_ui_ic_jellyseerr,
    showSkipButton = true,
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

  val linkHandlingPage = OnboardingPage(
    tag = "link_handling",
    title = UIText.ResourceText(R.string.feature_onboarding_link_handling_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_link_handling_page_description),
    image = null,
    showSkipButton = false,
    action = OnboardingAction.NavigateToLinkHandling,
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
    linkHandlingPage,
  )

  val newFeaturePages = emptyMap<Int, List<OnboardingPage>>()
}
