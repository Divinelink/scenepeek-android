package com.divinelink.feature.onboarding.manager

import com.divinelink.core.commons.platform.Platform
import com.divinelink.core.commons.platform.currentPlatform
import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.model.resources.Res
import com.divinelink.core.model.resources.core_model_ic_tmdb
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.resources.core_ui_ic_jellyseerr
import com.divinelink.feature.onboarding.resources.feature_onboarding_changelog
import com.divinelink.feature.onboarding.resources.feature_onboarding_jellyseerr_page_description
import com.divinelink.feature.onboarding.resources.feature_onboarding_jellyseerr_page_title
import com.divinelink.feature.onboarding.resources.feature_onboarding_link_handling_page_description
import com.divinelink.feature.onboarding.resources.feature_onboarding_link_handling_page_title
import com.divinelink.feature.onboarding.resources.feature_onboarding_tmdb_page_description
import com.divinelink.feature.onboarding.resources.feature_onboarding_tmdb_page_title
import com.divinelink.feature.onboarding.resources.feature_onboarding_v22_feature_profile
import com.divinelink.feature.onboarding.resources.feature_onboarding_v22_feature_tmdb_lists
import com.divinelink.feature.onboarding.resources.feature_onboarding_v22_fix_encryption
import com.divinelink.feature.onboarding.resources.feature_onboarding_v24_add_loading_indicator
import com.divinelink.feature.onboarding.resources.feature_onboarding_v24_retry_failed_api_calls
import com.divinelink.feature.onboarding.resources.feature_onboarding_v24_update_changelog
import com.divinelink.feature.onboarding.resources.feature_onboarding_v25_fix_favorite_status
import com.divinelink.feature.onboarding.resources.feature_onboarding_v25_media_action_menu
import com.divinelink.feature.onboarding.resources.feature_onboarding_v25_redesign_jellyseerr_login
import com.divinelink.feature.onboarding.resources.feature_onboarding_v25_redesign_ui_media_cards
import com.divinelink.feature.onboarding.resources.feature_onboarding_v25_support_emby_login
import com.divinelink.feature.onboarding.resources.feature_onboarding_v26_jellyseerr_request_advanced_settings
import com.divinelink.feature.onboarding.resources.feature_onboarding_v26_jellyseerr_request_advanced_settings_extra
import com.divinelink.feature.onboarding.resources.feature_onboarding_v27_jellyseerr_encryption
import com.divinelink.feature.onboarding.resources.feature_onboarding_v27_jellyseerr_requests_screen
import com.divinelink.feature.onboarding.resources.feature_onboarding_v27_tmdb_display_item_status_when_adding_to_lists
import com.divinelink.feature.onboarding.resources.feature_onboarding_v28_discover_screen
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_discover_filters
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_discover_rating_filter
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_favorite_status_lists
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_grid_view
import com.divinelink.feature.onboarding.resources.feature_onboarding_welcome_page_description
import com.divinelink.feature.onboarding.resources.feature_onboarding_welcome_page_title
import com.divinelink.feature.onboarding.resources.Res as R

object IntroSections {

  val jellyseerr = IntroSection.Feature(
    title = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_jellyseerr_page_description),
    image = UiDrawable.core_ui_ic_jellyseerr,
    action = OnboardingAction.NavigateToJellyseerrLogin(isComplete = false),
  )

  val tmdb = IntroSection.Feature(
    title = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_tmdb_page_description),
    image = Res.drawable.core_model_ic_tmdb,
    action = OnboardingAction.NavigateToTMDBLogin(isComplete = false),
  )

  val linkHandling = IntroSection.Feature(
    title = UIText.ResourceText(R.string.feature_onboarding_link_handling_page_title),
    description = UIText.ResourceText(R.string.feature_onboarding_link_handling_page_description),
    action = OnboardingAction.NavigateToLinkHandling,
  )

  val onboardingSections = buildList {
    add(
      IntroSection.Header(
        title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
        description = UIText.ResourceText(R.string.feature_onboarding_welcome_page_description),
      ),
    )
    add(IntroSection.Spacer)
    add(IntroSection.SecondaryHeader.Features)
    add(tmdb)
    add(jellyseerr)
    if (currentPlatform == Platform.Android) {
      add(linkHandling)
    }
    add(IntroSection.GetStartedButton)
  }

  val v23 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.15.0"),
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_feature_tmdb_lists)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_feature_profile)),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v22_fix_encryption)),
  )

  val v24 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.16.0"),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v24_retry_failed_api_calls)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v24_add_loading_indicator)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v24_update_changelog)),
  )

  val v25 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.17.0"),
    IntroSection.SecondaryHeader.Features,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v25_support_emby_login)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v25_redesign_ui_media_cards)),
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v25_redesign_jellyseerr_login),
    ),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v25_media_action_menu)),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v25_fix_favorite_status)),
  )

  val v26 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.18.0"),
    IntroSection.SecondaryHeader.Features,
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v26_jellyseerr_request_advanced_settings),
    ),
    IntroSection.Text(
      UIText.ResourceText(
        R.string.feature_onboarding_v26_jellyseerr_request_advanced_settings_extra,
      ),
    ),
  )

  val v27 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.19.0"),
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v27_jellyseerr_requests_screen),
    ),
    IntroSection.SecondaryHeader.Features,
    IntroSection.Text(
      UIText.ResourceText(
        R.string.feature_onboarding_v27_tmdb_display_item_status_when_adding_to_lists,
      ),
    ),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v27_jellyseerr_encryption),
    ),
  )

  val v28 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.20.0"),
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v28_discover_screen),
    ),
  )

  val v29 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.21.0"),
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v29_grid_view)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v29_discover_rating_filter)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v29_favorite_status_lists)),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v29_discover_filters)),
  )

  /**
   * A map of changelog sections keyed by version code.
   */
  val changelogSections = mapOf(
    23 to v23,
    24 to v24,
    25 to v25,
    26 to v26,
    27 to v27,
    28 to v28,
    29 to v29,
  )
}
