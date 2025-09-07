package com.divinelink.feature.request.media

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.modal.jellyseerr.request.RequestMovieModal
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class RequestMovieModalTest : ComposeTest() {

  @Test
  fun `test show request movie dialog`() {
    var onDismissRequest = false
    var onConfirm = false

    setContentWithTheme {
      RequestMovieModal(
        title = "Taxi Driver",
        onDismissRequest = {
          onDismissRequest = true
        },
        onConfirm = {
          onConfirm = true
        },
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Modal.REQUEST_MOVIE).assertIsDisplayed()

    composeTestRule.onNodeWithTag(TestTags.Dialogs.REQUEST_MOVIE_BUTTON).performClick()

    composeTestRule
      .onNodeWithText("Do you want to request Taxi Driver?")
      .assertIsDisplayed()

    assertThat(onDismissRequest).isTrue()
    assertThat(onConfirm).isTrue()

    composeTestRule.onNodeWithText("Cancel").performClick()
    assertThat(onDismissRequest).isTrue()
  }
}
