package com.divinelink.scenepeek.details.ui.rate

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.model.details.MediaDetailsFactory
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.feature.details.media.ui.rate.RateDialogContent
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import com.divinelink.feature.details.R as detailsR

class RateDialogContentTest : ComposeTest() {

  @Test
  fun `test onSubmitRate`() {
    val ratingValue = mutableStateOf(5f)
    val movie = MediaDetailsFactory.FightClub()

    var submitClicked = false

    setContentWithTheme {
      RateDialogContent(
        value = ratingValue.value,
        onRateChanged = {},
        mediaTitle = movie.title,
        onSubmitRate = {
          submitClicked = true
        },
        onClearRate = {},
        canClearRate = true,
      )
    }

    val descriptionText = composeTestRule.activity.getString(
      detailsR.string.details__add_rating_description,
      movie.title,
    )

    val submitButtonText =
      composeTestRule.activity.getString(detailsR.string.details__submit_rating_button)

    with(composeTestRule) {
      onNodeWithText(descriptionText).assertExists()
      onNodeWithText(submitButtonText).performClick()

      assertThat(submitClicked).isTrue()
    }
  }

  @Test
  fun `test submit button is disabled on initial state with value 0`() {
    val ratingValue = mutableStateOf(0f)
    val movie = MediaDetailsFactory.FightClub()

    var submitClicked = false

    setContentWithTheme {
      RateDialogContent(
        value = ratingValue.value,
        onRateChanged = {},
        mediaTitle = movie.title,
        onSubmitRate = {
          submitClicked = true
        },
        onClearRate = {},
        canClearRate = true,
      )
    }

    val descriptionText = composeTestRule.activity.getString(
      detailsR.string.details__add_rating_description,
      movie.title,
    )

    val submitButtonText =
      composeTestRule.activity.getString(detailsR.string.details__submit_rating_button)

    with(composeTestRule) {
      onNodeWithText(descriptionText).assertExists()
      onNodeWithText(submitButtonText).assertIsNotEnabled()
      onNodeWithText(submitButtonText).performClick()

      assertThat(submitClicked).isFalse()
    }
  }

  @Test
  fun `test onDeleteRate`() {
    val ratingValue = mutableStateOf(5f)
    val movie = MediaDetailsFactory.FightClub()

    var deleteClicked = false

    setContentWithTheme {
      RateDialogContent(
        value = ratingValue.value,
        onRateChanged = {},
        mediaTitle = movie.title,
        onSubmitRate = {},
        onClearRate = {
          deleteClicked = true
        },
        canClearRate = true,
      )
    }

    val descriptionText = composeTestRule.activity.getString(
      detailsR.string.details__add_rating_description,
      movie.title,
    )

    val deleteButtonText =
      composeTestRule.activity.getString(detailsR.string.details__clear_my_rating)

    with(composeTestRule) {
      onNodeWithText(descriptionText).assertExists()
      onNodeWithText(deleteButtonText).performClick()

      assertThat(deleteClicked).isTrue()
    }
  }
}
