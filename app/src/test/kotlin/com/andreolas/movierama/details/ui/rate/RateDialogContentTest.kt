package com.andreolas.movierama.details.ui.rate

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andreolas.factories.MediaDetailsFactory
import com.andreolas.movierama.R
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Test

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
        canClearRate = true
      )
    }

    val descriptionText = composeTestRule.activity.getString(
      R.string.details__add_rating_description,
      movie.title
    )

    val submitButtonText =
      composeTestRule.activity.getString(R.string.details__submit_rating_button)

    with(composeTestRule) {
      onNodeWithText(descriptionText).assertExists()
      onNodeWithText(submitButtonText).performClick()

      assertThat(submitClicked).isTrue()
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
        canClearRate = true
      )
    }

    val descriptionText = composeTestRule.activity.getString(
      R.string.details__add_rating_description,
      movie.title
    )

    val deleteButtonText =
      composeTestRule.activity.getString(R.string.details__clear_my_rating)

    with(composeTestRule) {
      onNodeWithText(descriptionText).assertExists()
      onNodeWithText(deleteButtonText).performClick()

      assertThat(deleteClicked).isTrue()
    }
  }
}
