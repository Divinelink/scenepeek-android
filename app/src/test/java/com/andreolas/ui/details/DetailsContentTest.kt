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
import com.andreolas.movierama.R
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.model.VideoSite
import com.andreolas.movierama.details.ui.DetailsContent
import com.andreolas.movierama.details.ui.DetailsViewState
import com.andreolas.movierama.details.ui.MOVIE_DETAILS_SCROLLABLE_LIST_TAG
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.home.ui.LOADING_CONTENT_TAG
import com.andreolas.movierama.ui.TestTags
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.details.reviews.REVIEWS_SCROLLABLE_LIST
import com.andreolas.movierama.ui.components.details.videos.VIDEO_PLAYER_TAG
import com.andreolas.setContentWithTheme
import com.google.common.truth.Truth.assertThat
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
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
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
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
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
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
      )
    }

    composeTestRule
      .onNodeWithTag(REVIEWS_SCROLLABLE_LIST)
      .assertDoesNotExist()
  }

  @Test
  fun renderReviewsTest() {
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
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
      )
    }

    val reviewsTitle = composeTestRule.activity.getString(R.string.details__reviews)

    composeTestRule
      .onNodeWithTag(MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
      .performScrollToNode(
        hasText(reviewsTitle)
      )

    composeTestRule
      .onNodeWithTag(REVIEWS_SCROLLABLE_LIST)
      .assertIsDisplayed()

    composeTestRule
      .onAllNodesWithText(reviews[0].content)[0]
      .performClick()
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
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
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
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
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
          userRating = "8",
          mediaDetails = MediaDetailsFactory.FightClub(),
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
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
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
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

  @Test
  fun `test rate dialog is visible`() {
    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          showRateDialog = true
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onSubmitRate = {},
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
      )
    }

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_DIALOG
    ).assertIsDisplayed()
  }

  @Test
  fun `test rate dialog onSubmitRate`() {
    var onSubmitRate = false

    setContentWithTheme {
      DetailsContent(
        viewState = DetailsViewState(
          movieId = 0,
          mediaType = MediaType.MOVIE,
          mediaDetails = MediaDetailsFactory.FightClub(),
          showRateDialog = true
        ),
        onNavigateUp = {},
        onMarkAsFavoriteClicked = {},
        onSimilarMovieClicked = {},
        onSubmitRate = {
          onSubmitRate = true
        },
        onConsumeSnackbar = {},
        onDismissBottomSheet = {},
        onAddRateClicked = {},
      )
    }

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_DIALOG
    ).assertIsDisplayed()

    val submitText = composeTestRule.activity.getString(R.string.details__submit_rating_button)

    composeTestRule.onNodeWithText(
      text = submitText,
      useUnmergedTree = true
    ).performClick()

    assertThat(onSubmitRate).isTrue()
  }

  private val reviews = ReviewFactory.ReviewList()
}
