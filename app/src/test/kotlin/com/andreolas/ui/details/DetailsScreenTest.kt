package com.andreolas.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.lifecycle.SavedStateHandle
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.andreolas.movierama.fakes.usecase.FakeGetMediaDetailsUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeAddToWatchlistUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeDeleteRatingUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeFetchAccountMediaDetailsUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeSubmitRatingUseCase
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.arguments.CreditsNavArguments
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.factories.model.details.MediaDetailsFactory
import com.divinelink.core.testing.factories.model.media.MediaItemFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.details.similar.SIMILAR_MOVIES_SCROLLABLE_LIST
import com.divinelink.feature.credits.screens.destinations.CreditsScreenDestination
import com.divinelink.feature.details.media.ui.DetailsScreen
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.media.ui.MOVIE_DETAILS_SCROLLABLE_LIST_TAG
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.divinelink.core.ui.R as uiR
import com.divinelink.feature.details.R as detailsR

class DetailsScreenTest : ComposeTest() {

  @Test
  fun navigateToAnotherDetailsScreen() {
    val getMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val fetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val requestMediaUseCase = FakeRequestMediaUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    destinationsNavigator.navigate(
      direction = DetailsScreenDestination(
        DetailsNavArguments(
          id = 0,
          mediaType = MediaType.MOVIE.name,
          isFavorite = false,
        ),
      ),
    )

    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(
        Result.success(AccountMediaDetailsFactory.Rated()),
      ),
    )

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub(),
          ),
        ),
        Result.success(
          MediaDetailsResult.SimilarSuccess(
            similar = MediaItemFactory.MoviesList(),
          ),
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        navigator = destinationsNavigator,
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = DetailsViewModel(
          getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          submitRatingUseCase = submitRateUseCase.mock,
          deleteRatingUseCase = deleteRatingUseCase.mock,
          addToWatchlistUseCase = addToWatchlistUseCase.mock,
          requestMediaUseCase = requestMediaUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 0,
              "isFavorite" to false,
              "mediaType" to MediaType.MOVIE.value,
            ),
          ),
        ),
      )
    }

    composeTestRule
      .onNodeWithTag(MOVIE_DETAILS_SCROLLABLE_LIST_TAG)
      .performScrollToNode(
        matcher = hasText(
          MediaItemFactory.MoviesList()[0].name,
        ),
      )

    composeTestRule
      .onNodeWithTag(SIMILAR_MOVIES_SCROLLABLE_LIST)
      .performScrollToNode(
        matcher = hasText(MediaItemFactory.MoviesList()[0].name),
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
        ),
      ),
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
        ),
      ),
    )
  }

  @Test
  fun `test rate dialog is visible when your rating is clicked`() = runTest {
    val getMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val requestMediaUseCase = FakeRequestMediaUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.AccountDetailsSuccess(
            accountDetails = AccountMediaDetailsFactory.Rated(),
          ),
        ),
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub(),
          ),
        ),

      ),
    )

    val viewModel = DetailsViewModel(
      getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
      onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
      submitRatingUseCase = submitRateUseCase.mock,
      deleteRatingUseCase = deleteRatingUseCase.mock,
      addToWatchlistUseCase = addToWatchlistUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE.value,
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        navigator = destinationsNavigator,
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = viewModel,
      )
    }

    composeTestRule.onNodeWithTag(
      testTag = TestTags.Details.YOUR_RATING,
      useUnmergedTree = true,
    ).performClick()

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_DIALOG,
    ).assertIsDisplayed()
  }

  @Test
  fun `test rate dialog onSubmitRate`() = runTest {
    val getMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val fetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val requestMediaUseCase = FakeRequestMediaUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(Result.success(AccountMediaDetailsFactory.NotRated())),
    )

    submitRateUseCase.mockSubmitRate(
      response = flowOf(Result.success(Unit)),
    )

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub(),
          ),
        ),
      ),
    )

    val viewModel = DetailsViewModel(
      getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
      onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
      submitRatingUseCase = submitRateUseCase.mock,
      deleteRatingUseCase = deleteRatingUseCase.mock,
      addToWatchlistUseCase = addToWatchlistUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE.value,
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        navigator = destinationsNavigator,
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = viewModel,
      )
    }

    val addRatingText = composeTestRule.activity.getString(detailsR.string.details__add_rating)

    composeTestRule.onNodeWithTag(
      TestTags.Details.YOUR_RATING,
      useUnmergedTree = true,
    ).assertDoesNotExist()

    composeTestRule.onNodeWithText(
      text = addRatingText,
      useUnmergedTree = true,
    ).assertIsDisplayed().performClick()

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_DIALOG,
    ).assertIsDisplayed()

    composeTestRule.onNodeWithTag(
      TestTags.Details.RATE_SLIDER,
    ).assertExists().performTouchInput {
      swipeRight()
    }

    val submitRatingText = composeTestRule
      .activity.getString(detailsR.string.details__submit_rating_button)

    composeTestRule.onNodeWithText(submitRatingText).performClick()

    composeTestRule.onNodeWithTag(
      TestTags.Details.YOUR_RATING,
      useUnmergedTree = true,
    ).assertIsDisplayed()
  }

  @Test
  fun `test navigate to credits screen with tv credits`() {
    val getMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val requestMediaUseCase = FakeRequestMediaUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    var verifyNavigatedToCredits: Pair<Boolean, CreditsNavArguments?> = false to null
    var verifyNavigatedToDetails = false

    // Initial navigation to Details screen
    destinationsNavigator.navigate(
      direction = DetailsScreenDestination(
        DetailsNavArguments(
          id = 2316,
          mediaType = MediaType.TV.name,
          isFavorite = false,
        ),
      ),
    )

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.TheOffice(),
          ),
        ),
        Result.success(
          MediaDetailsResult.CreditsSuccess(
            aggregateCredits = AggregatedCreditsFactory.credits(),
          ),
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        navigator = destinationsNavigator,
        onNavigateToAccountSettings = {
          destinationsNavigator.navigate(
            direction = AccountSettingsScreenDestination(),
          )
        },
        onNavigateToCredits = {
          destinationsNavigator.navigate(
            direction = CreditsScreenDestination(
              CreditsNavArguments(id = it.id, mediaType = it.mediaType),
            ),
          )
        },
        viewModel = DetailsViewModel(
          getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          submitRatingUseCase = submitRateUseCase.mock,
          deleteRatingUseCase = deleteRatingUseCase.mock,
          addToWatchlistUseCase = addToWatchlistUseCase.mock,
          requestMediaUseCase = requestMediaUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 2316,
              "isFavorite" to false,
              "mediaType" to MediaType.TV.value,
            ),
          ),
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.core_ui_view_all))
        .performScrollTo()
        .assertIsDisplayed()
        .performClick()

      destinationsNavigator.verifyNavigatedToDirection(
        expectedDirection = CreditsScreenDestination(
          CreditsNavArguments(id = 2316, mediaType = MediaType.TV),
        ),
      )

      // Navigate up from Credits screen
      onNodeWithContentDescription(
        getString(uiR.string.core_ui_navigate_up_button_content_description),
      ).assertIsDisplayed().performClick()

      destinationsNavigator.verifyNavigatedToDirection(
        expectedDirection = DetailsScreenDestination(
          DetailsNavArguments(
            id = 2316,
            isFavorite = false,
            mediaType = MediaType.TV.name,
          ),
        ),
      )
    }
  }

  @Test
  fun `test viewAll credits does not exist without tv credits`() {
    val getMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
    val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
    val submitRateUseCase = FakeSubmitRatingUseCase()
    val deleteRatingUseCase = FakeDeleteRatingUseCase()
    val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
    val requestMediaUseCase = FakeRequestMediaUseCase()
    val destinationsNavigator = FakeDestinationsNavigator()

    // Initial navigation to Details screen
    destinationsNavigator.navigate(
      direction = DetailsScreenDestination(
        DetailsNavArguments(
          id = 2316,
          mediaType = MediaType.TV.name,
          isFavorite = false,
        ),
      ),
    )

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.TheOffice(),
          ),
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        navigator = destinationsNavigator,
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = DetailsViewModel(
          getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          submitRatingUseCase = submitRateUseCase.mock,
          deleteRatingUseCase = deleteRatingUseCase.mock,
          addToWatchlistUseCase = addToWatchlistUseCase.mock,
          requestMediaUseCase = requestMediaUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 2316,
              "isFavorite" to false,
              "mediaType" to MediaType.TV.value,
            ),
          ),
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.core_ui_view_all)).assertDoesNotExist()
    }
  }
}
