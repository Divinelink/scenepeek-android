package com.divinelink.core.ui.rating

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import kotlin.test.Test

class IMDbRatingItemTest : ComposeTest() {

  @Test
  fun `test IMDbRating score is visible when ratingDetails is Score`() {
    val ratingDetails = RatingDetails.Score(8.0, 100)

    setContentWithTheme {
      IMDbRatingItem(ratingDetails = ratingDetails)
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Rating.IMDB_RATING).assertExists()
      onNodeWithText("8.0").assertExists()
      onNodeWithText(" / 10").assertExists()
      onNodeWithText("100").assertExists()
    }
  }

  @Test
  fun `test IMDbRating Skeleton is visible when ratingDetails is Initial`() {
    setContentWithTheme {
      IMDbRatingItem(ratingDetails = RatingDetails.Initial)
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Rating.IMDB_RATING).assertIsDisplayed()
      onNodeWithTag(TestTags.Rating.IMDB_RATING_SKELETON).assertIsDisplayed()
    }
  }

  @Test
  fun `test IMDbRating score is not visible when ratingDetails is Unavailable`() {
    setContentWithTheme {
      IMDbRatingItem(ratingDetails = RatingDetails.Unavailable)
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Rating.IMDB_RATING).assertIsDisplayed()
      onNodeWithText("-").assertExists()
    }
  }
}
