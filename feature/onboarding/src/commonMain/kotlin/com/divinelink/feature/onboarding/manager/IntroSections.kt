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
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_discover_filters
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_discover_rating_filter
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_favorite_status_lists
import com.divinelink.feature.onboarding.resources.feature_onboarding_v29_grid_view
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_convert_app_to_multiplatform
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_display_fullscreen_posters
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_external_ratings
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_fix_encryption
import com.divinelink.feature.onboarding.resources.feature_onboarding_v30_fix_jellyfin_auth_with_empty_passwords
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_add_custom_color_option
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_add_detailed_csrf_warning
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_add_detailed_error_messages_for_seerr
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_improve_search
import com.divinelink.feature.onboarding.resources.feature_onboarding_v32_show_year_on_search
import com.divinelink.feature.onboarding.resources.feature_onboarding_v33_fix_duplicate_ids_crash
import com.divinelink.feature.onboarding.resources.feature_onboarding_v33_redesign_home_screen
import com.divinelink.feature.onboarding.resources.feature_onboarding_v33_update_favorites
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

  val v30 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.22.0"),
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v30_convert_app_to_multiplatform),
    ),
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v30_display_fullscreen_posters),
    ),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v30_external_ratings)),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v30_fix_jellyfin_auth_with_empty_passwords),
    ),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v30_fix_encryption)),
  )

  val v32 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.23.0"),
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v32_improve_search)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v32_add_custom_color_option)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v32_show_year_on_search)),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v32_add_detailed_error_messages_for_seerr),
    ),
    IntroSection.Text(
      UIText.ResourceText(R.string.feature_onboarding_v32_add_detailed_csrf_warning),
    ),
  )

  val v33 = listOf(
    IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
    IntroSection.WhatsNew("v0.24.0"),
    IntroSection.SecondaryHeader.Added,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v33_redesign_home_screen)),
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v33_update_favorites)),
    IntroSection.SecondaryHeader.Fixed,
    IntroSection.Text(UIText.ResourceText(R.string.feature_onboarding_v33_fix_duplicate_ids_crash)),
  )

  /**
   * A map of changelog sections keyed by version code.
   */
  val changelogSections = mapOf(
    29 to v29,
    30 to v30,
    32 to v32,
    33 to v33
  )
}
