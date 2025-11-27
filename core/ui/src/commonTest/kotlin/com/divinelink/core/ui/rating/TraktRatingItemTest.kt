package com.divinelink.core.ui.rating

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.TestTags
import kotlin.test.Test

class TraktRatingItemTest : ComposeTest() {

  @Test
  fun `test TraktRatingItem score is visible when ratingDetails is Score`() = uiTest {
    val ratingDetails = RatingDetails.Score(8.0, 100)

    setContentWithTheme {
      TraktRatingItem(ratingDetails = ratingDetails)
    }

    onNodeWithTag(TestTags.Rating.TRAKT_RATING).assertExists()
    onNodeWithText("80%").assertExists()
    onNodeWithText("100 votes").assertExists()
  }

  @Test
  fun `test TraktRatingItem Skeleton is visible when ratingDetails is Initial`() = uiTest {
    setContentWithTheme {
      TraktRatingItem(ratingDetails = RatingDetails.Initial)
    }

    onNodeWithTag(TestTags.Rating.TRAKT_RATING).assertIsDisplayed()
    onNodeWithTag(TestTags.Rating.TRAKT_RATING_SKELETON).assertIsDisplayed()
  }

  @Test
  fun `test TraktRatingItem score is not visible when ratingDetails is Unavailable`() = uiTest {
    setContentWithTheme {
      TraktRatingItem(ratingDetails = RatingDetails.Unavailable)
    }

    onNodeWithTag(TestTags.Rating.TRAKT_RATING).assertIsDisplayed()
    onNodeWithText("-").assertExists()
  }
}
