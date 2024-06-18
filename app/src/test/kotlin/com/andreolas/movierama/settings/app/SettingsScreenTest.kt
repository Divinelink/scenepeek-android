package com.andreolas.movierama.settings.app

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.divinelink.core.testing.ComposeTest
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.SettingsScreen
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.AppearanceSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.HelpSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.SettingsScreenDestination
import org.junit.Test
import com.divinelink.core.ui.R as uiR

class SettingsScreenTest : ComposeTest() {

  @Test
  fun `test navigate to account screen and navigate up`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination()
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator
      )
    }

    with(composeTestRule) {
      onNodeWithText("Account").assertExists()

      onNodeWithText("Account").performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      AccountSettingsScreenDestination
    )

    val navigateUpContentDescription = composeTestRule.activity
      .getString(uiR.string.core_ui_navigate_up_button_content_description)

    with(composeTestRule) {
      onNodeWithContentDescription(navigateUpContentDescription).performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      SettingsScreenDestination
    )
  }

  @Test
  fun `test navigate to appearance screen`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination()
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator
      )
    }

    val appearanceString = composeTestRule.activity.getString(R.string.preferences__appearance)

    with(composeTestRule) {
      onNodeWithText(appearanceString).assertExists()

      onNodeWithText(appearanceString).performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      AppearanceSettingsScreenDestination
    )
  }

  @Test
  fun `test navigate to help screen`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = SettingsScreenDestination()
    )

    composeTestRule.setContent {
      SettingsScreen(
        navigator = destinationsNavigator
      )
    }

    val helpString = composeTestRule.activity.getString(R.string.preferences__help)

    with(composeTestRule) {
      onNodeWithText(helpString).assertExists()

      onNodeWithText(helpString).performClick()
    }

    destinationsNavigator.verifyNavigatedToDirection(
      HelpSettingsScreenDestination
    )
  }
}
