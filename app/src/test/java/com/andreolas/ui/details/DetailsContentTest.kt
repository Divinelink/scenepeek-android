package com.andreolas.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.andreolas.ComposeTest
import com.andreolas.factories.MediaDetailsFactory
import com.andreolas.factories.ReviewFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.andreolas.movierama.R
import com.andreolas.movierama.details.ui.DetailsContent
import com.andreolas.movierama.details.ui.DetailsViewState
import com.andreolas.movierama.details.ui.MOVIE_DETAILS_SCROLLABLE_LIST_TAG
import com.andreolas.movierama.home.ui.LOADING_CONTENT_TAG
import com.andreolas.movierama.ui.TestTags
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.details.reviews.REVIEWS_LIST
import com.andreolas.movierama.ui.components.details.videos.VIDEO_PLAYER_TAG
import com.andreolas.setContentWithTheme
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.details.video.VideoSite
import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DetailsContentTest : ComposeTest() {

  @Test
  fun clickMarkAsFavoriteTest() {
    var hasClickedMarkAsFavorite = false
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {
          hasClickedMarkAsFavorite = true
        },
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    val markAsFavoriteContentDescription = composeTestRule.activity
      .getString(R.string.mark_as_favorite_button_content_description)

    composeTestRule
      .onNodeWithContentDescription(markAsFavoriteContentDescription)
      .performClick()

    assertThat(hasClickedMarkAsFavorite).isTrue()
  }

  @Test
  fun loadingTest() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          isLoading = true,
          mediaType = MediaType.MOVIE,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    composeTestRule
      .onNodeWithTag(LOADING_CONTENT_TAG)
      .assertIsDisplayed()
  }

  @Test
  fun renderReviewsWithoutMovieDetailsTest() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          reviews = reviews,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    composeTestRule
      .onNodeWithTag(REVIEWS_LIST)
      .assertDoesNotExist()
  }

  @Test
  fun renderReviewsTest() = runTest {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          reviews = reviews,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    val reviewsTitle = composeTestRule.activity.getString(R.string.details__reviews)

    composeTestRule
      .onNodeWithTag(MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
      .performScrollToNode(
        hasText(reviewsTitle)
      )
      .assertIsDisplayed()

    with(composeTestRule) {
      onNodeWithTag(REVIEWS_LIST).assertIsDisplayed()
    }

    composeTestRule
      .onAllNodesWithText(reviews[0].content)[0]
      .performClick()
      .assertIsDisplayed()
  }

  @Test
  fun testErrorAlert() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          error = UIText.ResourceText(R.string.details__fatal_error_fetching_details)
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    val errorText = composeTestRule.activity
      .getString(R.string.details__fatal_error_fetching_details)

    val okText = composeTestRule.activity
      .getString(R.string.ok)

    composeTestRule
      .onNodeWithText(errorText)
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText(okText)
      .performClick()
  }

  @Test
  fun renderTrailerTest() {
    val youtubeTrailer = Video(
      id = "123",
      key = "123",
      name = "Trailer",
      site = VideoSite.YouTube,
      officialTrailer = true,
    )
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          trailer = youtubeTrailer,
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    composeTestRule
      .onNodeWithTag(VIDEO_PLAYER_TAG)
      .assertIsDisplayed()
  }

  @Test
  fun `given user rating rating score is displayed`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          userDetails = AccountMediaDetailsFactory.Rated(),
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    val userScore = composeTestRule.activity.getString(R.string.details__user_score, "7.3")

    composeTestRule
      .onNodeWithTag(
        testTag = TestTags.Details.YOUR_RATING,
        useUnmergedTree = true
      )
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText(userScore)
      .assertIsDisplayed()
  }

  @Test
  fun `given unrated movie add your rate button is displayed`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onConsumeSnackbar = {},
        onAddRateClicked = {},
        onAddToWatchlistClicked = {},
      )
    }

    val addYourRate = composeTestRule.activity.getString(R.string.details__add_rating)

    composeTestRule
      .onNodeWithText(
        text = addYourRate,
        useUnmergedTree = true
      )
      .assertIsDisplayed()
  }

  private val reviews = ReviewFactory.ReviewList()
}
