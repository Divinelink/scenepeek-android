package com.divinelink.feature.onboarding.manager

import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.feature.onboarding.R

object IntroSections {

  val jellyseerr = IntroSection.Feature(
    title = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_description),
    image = com.divinelink.core.ui.R.drawable.core_ui_ic_jellyseerr,
    action = OnboardingAction.NavigateToJellyseerrLogin(isComplete = false),
  )

  val tmdb = IntroSection.Feature(
    title = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_description),
    image = com.divinelink.core.model.R.drawable.core_model_ic_tmdb,
    action = OnboardingAction.NavigateToTMDBLogin(isComplete = false),
  )

  val linkHandling = IntroSection.Feature(
    title = UIText.ResourceText(R.string.feature_onboarding_link_handling_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_link_handling_page_description),
    action = OnboardingAction.NavigateToLinkHandling,
  )

  val onboardingSections = listOf(
    IntroSection.Header(
      title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
      description = UIText.ResourceText(R.string.feature_onboarding_welcome_page_description),
    ),
    IntroSection.Spacer,
    IntroSection.SecondaryHeader.Features,
    tmdb,
    jellyseerr,
    linkHandling,
  )

  val v22 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew,
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_feature_tmdb_lists)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_feature_profile)),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_fix_encryption)),
  )

  /**
   * A map of changelog sections keyed by version code.
   */
  val changelogSections = mapOf(
    22 to v22,
  )
}
