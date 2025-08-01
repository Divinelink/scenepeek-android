package com.divinelink.feature.settings.app

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.feature.settings.R
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class SettingsScreenTest : ComposeTest() {

  @Test
  fun `test navigate to account screen and navigate up`() {
    var navigatedToAccountSettings = false
    var navigatedUp = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.Back) {
            navigatedUp = true
          } else if (route is Navigation.AccountSettingsRoute) {
            navigatedToAccountSettings = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithText("Account").assertExists()
      onNodeWithText("Account").performClick()
    }

    assertThat(navigatedToAccountSettings).isTrue()

    val navigateUpContentDescription = composeTestRule.activity
      .getString(uiR.string.core_ui_navigate_up_button_content_description)

    with(composeTestRule) {
      onNodeWithContentDescription(navigateUpContentDescription).performClick()
    }

    assertThat(navigatedUp).isTrue()
  }

  @Test
  fun `test navigate to appearance screen`() {
    var navigatedToAppearanceSettings = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.AppearanceSettingsRoute) {
            navigatedToAppearanceSettings = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    val appearanceString = composeTestRule.activity.getString(R.string.preferences__appearance)

    with(composeTestRule) {
      onNodeWithText(appearanceString).assertExists()
      onNodeWithText(appearanceString).performClick()
    }

    assertThat(navigatedToAppearanceSettings).isTrue()
  }

  @Test
  fun `test navigate to link handling screen`() {
    var navigatedToLinkHandling = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.LinkHandlingSettingsRoute) {
            navigatedToLinkHandling = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    val linkHandlingSetting = getString(R.string.feature_settings_link_handling)

    with(composeTestRule) {
      onNodeWithText(linkHandlingSetting).assertExists()
      onNodeWithText(linkHandlingSetting).performClick()
    }

    assertThat(navigatedToLinkHandling).isTrue()
  }

  @Test
  fun `test navigate to details preference screen`() {
    var navigatedToDetailPreferencesSettings = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.DetailsPreferencesSettingsRoute) {
            navigatedToDetailPreferencesSettings = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    val detailsPreferencesString = getString(R.string.feature_settings_details_preferences)

    with(composeTestRule) {
      onNodeWithText(detailsPreferencesString).assertExists()

      onNodeWithText(detailsPreferencesString).performClick()
    }

    assertThat(navigatedToDetailPreferencesSettings).isTrue()
  }
}
