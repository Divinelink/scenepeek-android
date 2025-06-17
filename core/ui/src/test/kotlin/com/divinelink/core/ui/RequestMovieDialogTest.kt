package com.divinelink.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.ui.components.dialog.RequestMovieDialog
import com.divinelink.core.ui.components.dialog.ManageSeasonsModal
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class RequestDialogsTest : ComposeTest() {

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
        numberOfSeasons = 5,
        onRequestClick = {},
        onDismissRequest = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Dialogs.SELECT_SEASONS_DIALOG).assertIsDisplayed()
      onNodeWithText(getString(R.string.core_ui_request_series)).assertIsDisplayed()
      (1..5).forEach {
        onNodeWithText("Season $it").assertIsDisplayed()
      }
    }
  }

  @Test
  fun `test request tv show dialog confirm button is disabled without selected seasons`() {
    var onDismissRequest = false
    var onRequestClick = false

    composeTestRule.setContent {
      ManageSeasonsModal(
        numberOfSeasons = 5,
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
        numberOfSeasons = 5,
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

      onNodeWithText("Season 1").performClick()
      onNodeWithText("Season 2").performClick()
      onNodeWithText("Season 3").performClick()

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
        numberOfSeasons = 5,
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

      onNodeWithText(getString(R.string.core_ui_select_seasons_button))
        .assertDoesNotExist()

      onNodeWithTag(TestTags.RadioButton.SELECT_SEASON_RADIO_BUTTON.format(1)).assertIsDisplayed()
        .performClick()
      onNodeWithTag(TestTags.RadioButton.SELECT_SEASON_RADIO_BUTTON.format(2)).assertIsDisplayed()
        .performClick()
      onNodeWithTag(TestTags.RadioButton.SELECT_SEASON_RADIO_BUTTON.format(3)).assertIsDisplayed()
        .performClick()

      onNodeWithText(getString(R.string.core_ui_select_seasons_button))
        .assertIsDisplayed()
        .assertIsNotEnabled()
    }
  }
}
