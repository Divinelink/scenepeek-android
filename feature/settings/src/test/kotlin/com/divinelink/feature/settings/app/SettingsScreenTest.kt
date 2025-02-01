package com.divinelink.feature.settings.app

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.screens.destinations.AboutSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.AppearanceSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.DetailPreferencesSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.LinkHandlingSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.SettingsScreenDestination
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class SettingsScreenTest : ComposeTest() {

  @Test
  fun `test navigate to account screen and navigate up`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination(),
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator,
      )
    }

    with(composeTestRule) {
      onNodeWithText("Account").assertExists()

      onNodeWithText("Account").performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      AccountSettingsScreenDestination,
    )

    val navigateUpContentDescription = composeTestRule.activity
      .getString(uiR.string.core_ui_navigate_up_button_content_description)

    with(composeTestRule) {
      onNodeWithContentDescription(navigateUpContentDescription).performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      SettingsScreenDestination,
    )
  }

  @Test
  fun `test navigate to appearance screen`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination(),
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator,
      )
    }

    val appearanceString = composeTestRule.activity.getString(R.string.preferences__appearance)

    with(composeTestRule) {
      onNodeWithText(appearanceString).assertExists()

      onNodeWithText(appearanceString).performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      AppearanceSettingsScreenDestination,
    )
  }

  @Test
  fun `test navigate to link handling screen`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination(),
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator,
      )
    }

    val linkHandlingSetting = getString(R.string.feature_settings_link_handling)

    with(composeTestRule) {
      onNodeWithText(linkHandlingSetting).assertExists()

      onNodeWithText(linkHandlingSetting).performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      LinkHandlingSettingsScreenDestination,
    )
  }

  @Test
  fun `test navigate to help screen`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination(),
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator,
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

    destinationsNavigator.verifyNavigatedToDirection(
      AboutSettingsScreenDestination,
    )
  }

  @Test
  fun `test navigate to details preference screen`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination(),
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator,
      )
    }

    val detailsPreferencesString = getString(R.string.feature_settings_details_preferences)

    with(composeTestRule) {
      onNodeWithText(detailsPreferencesString).assertExists()

      onNodeWithText(detailsPreferencesString).performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(DetailPreferencesSettingsScreenDestination)
  }
}
