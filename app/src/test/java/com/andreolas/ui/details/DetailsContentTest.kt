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
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.details.reviews.REVIEWS_SCROLLABLE_LIST
import com.andreolas.movierama.ui.components.details.videos.VIDEO_PLAYER_TAG
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DetailsContentTest : ComposeTest() {

  @Test
  fun clickMarkAsFavoriteTest() {
    var hasClickedMarkAsFavorite = false
    composeTestRule
      .setContent {
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
    composeTestRule
      .setContent {
        DetailsContent(
          viewState = DetailsViewState(
            movieId = 0,
            isLoading = true,
            mediaType = MediaType.MOVIE,
          ),
          onNavigateUp = {},
          onMarkAsFavoriteClicked = {},
          onSimilarMovieClicked = {},
        )
      }

    composeTestRule
      .onNodeWithTag(LOADING_CONTENT_TAG)
      .assertIsDisplayed()
  }

  @Test
  fun renderReviewsWithoutMovieDetailsTest() {
    composeTestRule
      .setContent {
        DetailsContent(
          viewState = DetailsViewState(
            movieId = 0,
            mediaType = MediaType.MOVIE,
            reviews = reviews,
          ),
          onNavigateUp = {},
          onMarkAsFavoriteClicked = {},
          onSimilarMovieClicked = {},
        )
      }

    composeTestRule
      .onNodeWithTag(REVIEWS_SCROLLABLE_LIST)
      .assertDoesNotExist()
  }

  @Test
  fun renderReviewsTest() {
    composeTestRule
      .setContent {
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
    composeTestRule
      .setContent {
        DetailsContent(
          viewState = DetailsViewState(
            movieId = 0,
            mediaType = MediaType.MOVIE,
            error = UIText.ResourceText(R.string.details__fatal_error_fetching_details)
          ),
          onNavigateUp = {},
          onMarkAsFavoriteClicked = {},
          onSimilarMovieClicked = {},
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
    composeTestRule
      .setContent {
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
        )
      }

    composeTestRule
      .onNodeWithTag(VIDEO_PLAYER_TAG)
      .assertIsDisplayed()
  }

  private val reviews = ReviewFactory.ReviewList()
}
