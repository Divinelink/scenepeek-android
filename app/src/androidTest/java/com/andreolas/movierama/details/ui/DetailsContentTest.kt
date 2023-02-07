package com.andreolas.movierama.details.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.andreolas.movierama.R
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.home.ui.LOADING_CONTENT_TAG
import com.andreolas.movierama.ui.components.details.reviews.REVIEWS_SCROLLABLE_LIST
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class DetailsContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun clickMarkAsFavoriteTest() {
        var hasClickedMarkAsFavorite = false
        composeTestRule
            .setContent {
                DetailsContent(
                    viewState = DetailsViewState(
                        movieId = 0,
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
                        isLoading = true
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
                        movieDetails = movieDetails,
                        reviews = reviews,
                    ),
                    onNavigateUp = {},
                    onMarkAsFavoriteClicked = {},
                    onSimilarMovieClicked = {},
                )
            }

        val reviewsTitle = composeTestRule.activity
            .getString(R.string.details__reviews)

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

    private val reviews = (1..2).map {
        Review(
            authorName = "Author name $it",
            rating = 10,
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales " +
                "laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu" +
                "elit ut nunc convallis laoreet non ut libero. Suspendisse interdum placerat" +
                "risus vel ornare. Donec vehicula, turpis sed consectetur ullamcorper, ante" +
                "nunc egestas quam, ultricies adipiscing velit enim at nunc. Aenean id diam" +
                "neque. Praesent ut lacus sed justo viverra fermentum et ut sem. \n Fusce" +
                "convallis gravida lacinia. Integer semper dolor ut elit sagittis lacinia." +
                "Praesent sodales scelerisque eros at rhoncus. Duis posuere sapien vel ipsum" +
                "ornare interdum at eu quam. Vestibulum vel massa erat. Aenean quis sagittis" +
                "purus. Phasellus arcu purus, rutrum id consectetur non, bibendum at nibh.",
            date = "2022-10-22"
        )
    }
}
