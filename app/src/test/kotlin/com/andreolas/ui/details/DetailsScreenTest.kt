package com.andreolas.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.lifecycle.SavedStateHandle
import com.andreolas.factories.MediaDetailsFactory
import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.andreolas.movierama.fakes.usecase.FakeGetMoviesDetailsUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeAddToWatchlistUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeDeleteRatingUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeFetchAccountMediaDetailsUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeSubmitRatingUseCase
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.details.similar.SIMILAR_MOVIES_SCROLLABLE_LIST
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.ui.DetailsNavArguments
import com.divinelink.feature.details.ui.MovieDetailsResult
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.divinelink.core.ui.R as uiR
import com.divinelink.feature.details.R as detailsR

class DetailsScreenTest : ComposeTest() {

  @Test
  fun navigateToAnotherDetailsScreen() {
    val getMovieDetailsUseCase = FakeGetMoviesDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val fetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = DetailsScreenDestination(
        DetailsNavArguments(
          id = 0,
          mediaType = MediaType.MOVIE.name,
          isFavorite = false,
        )
      )
    )

    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(
        Result.success(AccountMediaDetailsFactory.Rated())
      )
    )

    getMovieDetailsUseCase.mockFetchMovieDetails(
      response = flowOf(
        Result.success(
          MovieDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub()
          )
        ),
        Result.success(
          MovieDetailsResult.SimilarSuccess(
            similar = MediaItemFactory.MoviesList(),
          )
        )
      )
    )

    setContentWithTheme {
      com.divinelink.feature.details.ui.DetailsScreen(
        navigator = destinationsNavigator,
        viewModel = com.divinelink.feature.details.ui.DetailsViewModel(
          getMovieDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          fetchAccountMediaDetailsUseCase = fetchAccountMediaDetailsUseCase.mock,
          submitRatingUseCase = submitRateUseCase.mock,
          deleteRatingUseCase = deleteRatingUseCase.mock,
          addToWatchlistUseCase = addToWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 0,
              "isFavorite" to false,
              "mediaType" to MediaType.MOVIE.value,
            )
          )
        )
      )
    }

    composeTestRule
      .onNodeWithTag(com.divinelink.feature.details.ui.MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
      .performScrollToNode(
        matcher = hasText(
          MediaItemFactory.MoviesList()[0].name
        )
      )

    composeTestRule
      .onNodeWithTag(SIMILAR_MOVIES_SCROLLABLE_LIST)
      .performScrollToNode(
        matcher = hasText(MediaItemFactory.MoviesList()[0].name)
      )

    composeTestRule
      .onNodeWithText(MediaItemFactory.MoviesList()[0].name)
      .assertIsDisplayed()
      .performClick()

    destinationsNavigator.verifyNavigatedToDirection(
      expectedDirection = DetailsScreenDestination(
        DetailsNavArguments(
          id = 0,
          isFavorite = false,
          mediaType = MediaType.MOVIE.name,
        )
      )
    )

    val navigateUpContentDescription = composeTestRule.activity
      .getString(uiR.string.core_ui_navigate_up_button_content_description)

    composeTestRule
      .onNodeWithContentDescription(navigateUpContentDescription)
      .performClick()

    destinationsNavigator.verifyNavigatedToDirection(
      expectedDirection = DetailsScreenDestination(
        DetailsNavArguments(
          id = 0,
          isFavorite = false,
          mediaType = MediaType.MOVIE.name,
        )
      )
    )
  }

  @Test
  fun `test rate dialog is visible when your rating is clicked`() = runTest {
    val getMovieDetailsUseCase = FakeGetMoviesDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val fetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(Result.success(AccountMediaDetailsFactory.Rated()))
    )

    getMovieDetailsUseCase.mockFetchMovieDetails(
      response = flowOf(
        Result.success(
          MovieDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub()
          )
        ),
      )
    )

    val viewModel = com.divinelink.feature.details.ui.DetailsViewModel(
      getMovieDetailsUseCase = getMovieDetailsUseCase.mock,
      onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
      fetchAccountMediaDetailsUseCase = fetchAccountMediaDetailsUseCase.mock,
      submitRatingUseCase = submitRateUseCase.mock,
      deleteRatingUseCase = deleteRatingUseCase.mock,
      addToWatchlistUseCase = addToWatchlistUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE.value,
        )
      )
    )

    setContentWithTheme {
      com.divinelink.feature.details.ui.DetailsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    composeTestRule.onNodeWithTag(
      testTag = TestTags.Details.YOUR_RATING,
      useUnmergedTree = true
    ).performClick()

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_DIALOG
    ).assertIsDisplayed()
  }

  @Test
  fun `test rate dialog onSubmitRate`() = runTest {
    val getMovieDetailsUseCase = FakeGetMoviesDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val fetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(Result.success(AccountMediaDetailsFactory.NotRated()))
    )

    submitRateUseCase.mockSubmitRate(
      response = flowOf(Result.success(Unit))
    )

    getMovieDetailsUseCase.mockFetchMovieDetails(
      response = flowOf(
        Result.success(
          MovieDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub()
          )
        ),
      )
    )

    val viewModel = com.divinelink.feature.details.ui.DetailsViewModel(
      getMovieDetailsUseCase = getMovieDetailsUseCase.mock,
      onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
      fetchAccountMediaDetailsUseCase = fetchAccountMediaDetailsUseCase.mock,
      submitRatingUseCase = submitRateUseCase.mock,
      deleteRatingUseCase = deleteRatingUseCase.mock,
      addToWatchlistUseCase = addToWatchlistUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE.value,
        )
      )
    )

    setContentWithTheme {
      com.divinelink.feature.details.ui.DetailsScreen(
        navigator = destinationsNavigator,
        viewModel = viewModel
      )
    }

    val addRatingText = composeTestRule.activity.getString(detailsR.string.details__add_rating)

    composeTestRule.onNodeWithTag(
      TestTags.Details.YOUR_RATING,
      useUnmergedTree = true
    ).assertDoesNotExist()

    composeTestRule.onNodeWithText(
      text = addRatingText,
      useUnmergedTree = true
    ).assertIsDisplayed().performClick()

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_DIALOG
    ).assertIsDisplayed()

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_SLIDER
    ).assertExists().performTouchInput {
      swipeRight()
    }

    val submitRatingText = composeTestRule
      .activity.getString(detailsR.string.details__submit_rating_button)

    composeTestRule.onNodeWithText(submitRatingText).performClick()

    composeTestRule.onNodeWithTag(
      TestTags.Details.YOUR_RATING,
      useUnmergedTree = true
    ).assertIsDisplayed()
  }
}
