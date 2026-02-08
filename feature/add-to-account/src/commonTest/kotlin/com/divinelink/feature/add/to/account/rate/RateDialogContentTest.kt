package com.divinelink.feature.add.to.account.rate

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.fromHtml
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.add_rating_description
import com.divinelink.feature.add.to.account.resources.clear_my_rating
import com.divinelink.feature.add.to.account.resources.submit_rating_button
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class RateDialogContentTest : ComposeTest() {

  @Test
  fun `test onSubmitRate`() = uiTest {
    val ratingValue = mutableStateOf(5f)
    val movie = MediaDetailsFactory.FightClub()

    var submitClicked = false

    setContentWithTheme {
      RateDialogContent(
        value = ratingValue.value,
        mediaTitle = movie.title,
        onSubmitRate = {
          submitClicked = true
        },
        onClearRate = {},
        canClearRate = true,
      )
    }

    val descriptionText = getString(
      Res.string.add_rating_description,
      movie.title,
    ).fromHtml()

    val submitButtonText = getString(Res.string.submit_rating_button)

    onNodeWithText(descriptionText.text).assertExists()
    onNodeWithText(submitButtonText).performClick()

    submitClicked shouldBe true
  }

  @Test
  fun `test submit button is disabled on initial state with value 0`() = uiTest {
    val ratingValue = mutableStateOf(0f)
    val movie = MediaDetailsFactory.FightClub()

    var submitClicked = false

    setContentWithTheme {
      RateDialogContent(
        value = ratingValue.value,
        mediaTitle = movie.title,
        onSubmitRate = { submitClicked = true },
        onClearRate = {},
        canClearRate = true,
      )
    }

    val descriptionText = getString(
      Res.string.add_rating_description,
      movie.title,
    ).fromHtml()

    val submitButtonText = getString(Res.string.submit_rating_button)

    onNodeWithText(descriptionText.text).assertExists()
    onNodeWithText(submitButtonText).assertIsNotEnabled()
    onNodeWithText(submitButtonText).performClick()

    submitClicked shouldBe false
  }

  @Test
  fun `test onDeleteRate`() = uiTest {
    val ratingValue = mutableStateOf(5f)
    val movie = MediaDetailsFactory.FightClub()

    var deleteClicked = false

    setContentWithTheme {
      RateDialogContent(
        value = ratingValue.value,
        mediaTitle = movie.title,
        onSubmitRate = {},
        onClearRate = { deleteClicked = true },
        canClearRate = true,
      )
    }

    val descriptionText =
      getString(Res.string.add_rating_description, movie.title).fromHtml()

    val deleteButtonText = getString(Res.string.clear_my_rating)

    onNodeWithText(descriptionText.text).assertExists()
    onNodeWithText(deleteButtonText).performClick()

    deleteClicked shouldBe true
  }
}
