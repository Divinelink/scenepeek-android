package com.divinelink.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.details.season.SeasonFactory
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.ui.components.dialog.ManageSeasonsModal
import com.divinelink.core.ui.components.dialog.RequestMovieDialog
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class RequestModalsTest : ComposeTest() {

  @Test
  fun `test show request movie dialog`() {
    var onDismissRequest = false
    var onConfirm = false

    composeTestRule.setContent {
      RequestMovieDialog(
        title = "Taxi Driver",
        onDismissRequest = {
          onDismissRequest = true
        },
        onConfirm = {
          onConfirm = true
        },
      )
    }

    composeTestRule.onNodeWithText("Request movie").assertIsDisplayed()

    composeTestRule.onNodeWithText("Confirm").performClick()

    composeTestRule.onNodeWithText("Are you sure you want to request Taxi Driver?")
      .assertIsDisplayed()

    assertThat(onDismissRequest).isFalse()
    assertThat(onConfirm).isTrue()

    composeTestRule.onNodeWithText("Cancel").performClick()
    assertThat(onDismissRequest).isTrue()
  }

  @Test
  fun `test show request tv show dialog`() {
    composeTestRule.setContent {
      ManageSeasonsModal(
        seasons = SeasonFactory.allWithStatus(),
        onRequestClick = {},
        onDismissRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_request_series)).assertIsDisplayed()
    }
  }

  @Test
  fun `test request tv show dialog confirm button is disabled without selected seasons`() {
    composeTestRule.setContent {
      ManageSeasonsModal(
        seasons = SeasonFactory.allWithStatus(),
        onRequestClick = {},
        onDismissRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_cancel)).assertIsDisplayed().assertIsEnabled()
      onNodeWithText(getString(R.string.core_ui_select_seasons_button))
        .assertIsDisplayed()
        .assertIsNotEnabled()
    }
  }

  @Test
  fun `test request tv show dialog confirm button is enabled with selected seasons`() {
    var onDismissRequest = false
    var onRequestClick = false

    composeTestRule.setContent {
      ManageSeasonsModal(
        seasons = SeasonFactory.all(),
        onRequestClick = {
          onRequestClick = true
        },
        onDismissRequest = {
          onDismissRequest = true
        },
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_cancel)).assertIsDisplayed().assertIsEnabled()

      onNodeWithTag(TestTags.Dialogs.SEASON_ROW.format(1)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_ROW.format(2)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(3)).performClick()

      onNodeWithText("Request 3 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithText("Season 1").performClick()

      onNodeWithText("Request 2 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithText("Season 2").performClick()

      onNodeWithText("Request 1 season").assertIsDisplayed().assertIsEnabled().performClick()

      assertThat(onRequestClick).isTrue()
      assertThat(onDismissRequest).isTrue()
    }
  }

  @Test
  fun `test re-selecting seasons removes them`() {
    composeTestRule.setContent {
      ManageSeasonsModal(
        seasons = SeasonFactory.all(),
        onRequestClick = {},
        onDismissRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_cancel)).assertIsDisplayed().assertIsEnabled()

      onNodeWithText(getString(R.string.core_ui_select_seasons_button))
        .assertIsDisplayed()
        .assertIsNotEnabled()

      onNodeWithText("Request 3 seasons").assertDoesNotExist()

      onNodeWithText("Season 1").performClick()
      onNodeWithText("Season 2").performClick()
      onNodeWithText("Season 3").performClick()

      onNodeWithText("Request 3 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithText(getString(R.string.core_ui_select_seasons_button)).assertDoesNotExist()

      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(1)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(2)).performClick()
      onNodeWithTag(TestTags.Dialogs.SEASON_SWITCH.format(3)).performClick()

      onNodeWithText(getString(R.string.core_ui_select_seasons_button))
        .assertIsDisplayed()
        .assertIsNotEnabled()
    }
  }

  @Test
  fun `test request tv show dialog toggle all switch`() {
    composeTestRule.setContent {
      ManageSeasonsModal(
        seasons = SeasonFactory.all(),
        onRequestClick = {},
        onDismissRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_select_seasons_button)).assertIsDisplayed()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons").assertIsDisplayed().assertIsEnabled()

      // Toggle it back to unselect all seasons
      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText(getString(R.string.core_ui_select_seasons_button)).assertIsDisplayed()
    }
  }

  @Test
  fun `test request tv show dialog toggle all after already have selected few seasons`() {
    composeTestRule.setContent {
      ManageSeasonsModal(
        seasons = SeasonFactory.all(),
        onRequestClick = {},
        onDismissRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_select_seasons_button)).assertIsDisplayed()

      onNodeWithText("Season 1").performClick()
      onNodeWithText("Season 2").performClick()
      onNodeWithText("Season 3").performClick()

      onNodeWithText("Request 3 seasons").assertIsDisplayed().assertIsEnabled()

      onNodeWithTag(TestTags.Dialogs.TOGGLE_ALL_SEASONS_SWITCH).performClick()
      onNodeWithText("Request 9 seasons").assertIsDisplayed().assertIsEnabled()
    }
  }

  @Test
  fun `test request tv show modal already processed seasons are not clickable`() {
    composeTestRule.setContent {
      ManageSeasonsModal(
        seasons = SeasonFactory.allWithStatus(),
        onRequestClick = {},
        onDismissRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_select_seasons_button)).assertIsDisplayed()

      onNodeWithText("Season 1").performClick()
      onNodeWithText("Season 2").performClick()
      onNodeWithText("Season 3").performClick()

      onNodeWithText(getString(R.string.core_ui_select_seasons_button)).assertIsDisplayed()
    }
  }
}
