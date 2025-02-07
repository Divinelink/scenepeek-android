package com.divinelink.feature.settings.app

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class SettingsScreenTest : ComposeTest() {

  @Test
  fun `test navigate to account screen and navigate up`() {
    var navigatedToAccountSettings = false
    var navigatedUp = false
    composeTestRule.setContent {
      SettingsScreen(
        onNavigateUp = {
          navigatedUp = true
        },
        onNavigateToAccountSettings = {
          navigatedToAccountSettings = true
        },
        onNavigateToAppearanceSettings = { },
        onNavigateToDetailPreferencesSettings = { },
        onNavigateToLinkHandling = { },
        onNavigateToAboutSettings = { },
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
    composeTestRule.setContent {
      SettingsScreen(
        onNavigateUp = {},
        onNavigateToAccountSettings = {},
        onNavigateToAppearanceSettings = {
          navigatedToAppearanceSettings = true
        },
        onNavigateToDetailPreferencesSettings = {},
        onNavigateToLinkHandling = {},
        onNavigateToAboutSettings = {},
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
    composeTestRule.setContent {
      SettingsScreen(
        onNavigateUp = {},
        onNavigateToAccountSettings = {},
        onNavigateToAppearanceSettings = {},
        onNavigateToDetailPreferencesSettings = {},
        onNavigateToLinkHandling = {
          navigatedToLinkHandling = true
        },
        onNavigateToAboutSettings = {},
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
  fun `test navigate to about screen`() {
    var navigatedToAbout = false
    composeTestRule.setContent {
      SettingsScreen(
        onNavigateUp = {},
        onNavigateToAccountSettings = {},
        onNavigateToAppearanceSettings = {},
        onNavigateToDetailPreferencesSettings = {},
        onNavigateToLinkHandling = {},
        onNavigateToAboutSettings = {
          navigatedToAbout = true
        },
      )
    }

    val aboutString = getString(R.string.feature_settings_about)

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.SCREEN_CONTENT).performScrollToNode(
        hasText(aboutString),
      )
      onNodeWithText(aboutString).assertExists()

      onNodeWithText(aboutString).performClick()
    }

    assertThat(navigatedToAbout).isTrue()
  }

  @Test
  fun `test navigate to details preference screen`() {
    var navigatedToDetailPreferencesSettings = false
    composeTestRule.setContent {
      SettingsScreen(
        onNavigateUp = {},
        onNavigateToAccountSettings = {},
        onNavigateToAppearanceSettings = {},
        onNavigateToDetailPreferencesSettings = {
          navigatedToDetailPreferencesSettings = true
        },
        onNavigateToLinkHandling = {},
        onNavigateToAboutSettings = {},
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
