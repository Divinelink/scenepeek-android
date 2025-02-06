package com.divinelink.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.CreditsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.core.testing.usecase.TestFetchAllRatingsUseCase
import com.divinelink.core.testing.usecase.TestSpoilersObfuscationUseCase
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.divinelink.feature.details.media.ui.DetailsScreen
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.scenepeek.fakes.usecase.FakeGetMediaDetailsUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeAddToWatchlistUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeDeleteRatingUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeFetchAccountMediaDetailsUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeSubmitRatingUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR
import com.divinelink.feature.details.R as detailsR

class DetailsScreenTest : ComposeTest() {

  private val getMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
  private val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val fetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
  private val submitRateUseCase = FakeSubmitRatingUseCase()
  private val deleteRatingUseCase = FakeDeleteRatingUseCase()
  private val addToWatchlistUseCase = FakeAddToWatchlistUseCase()
  private val requestMediaUseCase = FakeRequestMediaUseCase()
  private val fetchAllRatingsUseCase = TestFetchAllRatingsUseCase()
  private val spoilersObfuscationUseCase = TestSpoilersObfuscationUseCase().useCase()

  @Test
  fun navigateToAnotherDetailsScreen() {
    var navigatedToDetails = false

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
            ratingSource = RatingSource.TMDB,
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
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = DetailsViewModel(
          getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          submitRatingUseCase = submitRateUseCase.mock,
          deleteRatingUseCase = deleteRatingUseCase.mock,
          addToWatchlistUseCase = addToWatchlistUseCase.mock,
          requestMediaUseCase = requestMediaUseCase.mock,
          spoilersObfuscationUseCase = spoilersObfuscationUseCase,
          fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 0,
              "isFavorite" to false,
              "mediaType" to MediaType.MOVIE,
            ),
          ),
        ),
        onNavigateUp = {},
        onNavigateToDetails = {
          navigatedToDetails = true
        },
        onNavigateToPerson = {},
      )
    }

    composeTestRule
      .onNodeWithTag(TestTags.Details.CONTENT_LIST)
      .performScrollToNode(
        matcher = hasText(
          MediaItemFactory.MoviesList()[0].name,
        ),
      )

    composeTestRule
      .onNodeWithTag(TestTags.Details.SIMILAR_MOVIES_LIST)
      .performScrollToNode(
        matcher = hasText(MediaItemFactory.MoviesList()[0].name),
      )

    composeTestRule
      .onNodeWithText(MediaItemFactory.MoviesList()[0].name)
      .assertIsDisplayed()
      .performClick()

    val navigateUpContentDescription = composeTestRule.activity
      .getString(uiR.string.core_ui_navigate_up_button_content_description)

    composeTestRule
      .onNodeWithContentDescription(navigateUpContentDescription)
      .performClick()

    assertThat(navigatedToDetails).isTrue()
  }

  @Test
  fun `test rate dialog is visible when your rating is clicked`() = runTest {
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
            ratingSource = RatingSource.TMDB,
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
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE,
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToDetails = {},
        onNavigateToPerson = {},
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
            ratingSource = RatingSource.TMDB,
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
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE,
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToDetails = {},
        onNavigateToPerson = {},
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
    // Initial navigation to Details screen
    var navigatedToCredits = false
    var route: CreditsRoute? = null

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.TheOffice(),
            ratingSource = RatingSource.TMDB,
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
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {
          route = it
          navigatedToCredits = true
        },
        viewModel = DetailsViewModel(
          getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          submitRatingUseCase = submitRateUseCase.mock,
          deleteRatingUseCase = deleteRatingUseCase.mock,
          addToWatchlistUseCase = addToWatchlistUseCase.mock,
          requestMediaUseCase = requestMediaUseCase.mock,
          spoilersObfuscationUseCase = spoilersObfuscationUseCase,
          fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 2316,
              "isFavorite" to false,
              "mediaType" to MediaType.TV,
            ),
          ),
        ),
        onNavigateUp = {},
        onNavigateToDetails = {},
        onNavigateToPerson = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Details.CONTENT_LIST).performScrollToIndex(2)

      onNodeWithText(getString(R.string.core_ui_view_all))
        .assertIsDisplayed()
        .performClick()

      assertThat(navigatedToCredits).isTrue()
      assertThat(route).isEqualTo(
        CreditsRoute(
          id = 2316,
          mediaType = MediaType.TV,
        ),
      )

      // Navigate up from Credits screen
      onNodeWithContentDescription(
        getString(uiR.string.core_ui_navigate_up_button_content_description),
      ).assertIsDisplayed().performClick()
    }
  }

  @Test
  fun `test viewAll credits does not exist without tv credits`() {
    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.TheOffice(),
            ratingSource = RatingSource.TMDB,
          ),
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = DetailsViewModel(
          getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
          onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
          submitRatingUseCase = submitRateUseCase.mock,
          deleteRatingUseCase = deleteRatingUseCase.mock,
          addToWatchlistUseCase = addToWatchlistUseCase.mock,
          requestMediaUseCase = requestMediaUseCase.mock,
          spoilersObfuscationUseCase = spoilersObfuscationUseCase,
          fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 2316,
              "isFavorite" to false,
              "mediaType" to MediaType.TV,
            ),
          ),
        ),
        onNavigateUp = {},
        onNavigateToDetails = {},
        onNavigateToPerson = {},
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.core_ui_view_all)).assertDoesNotExist()
    }
  }

  @Test
  fun `test onViewAllRatingsClick shows AllRatingsModalBottomSheet`() = runTest {
    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(Result.success(AccountMediaDetailsFactory.NotRated())),
    )

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub(),
            ratingSource = RatingSource.TMDB,
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
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE,
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = viewModel,
        onNavigateUp = {},
        onNavigateToDetails = {},
        onNavigateToPerson = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Rating.DETAILS_RATING_BUTTON).assertIsDisplayed().performClick()

      onNodeWithTag(TestTags.Rating.ALL_RATINGS_BOTTOM_SHEET).assertIsDisplayed()
    }
  }

  @Test
  fun `test onViewAllRatingsClick `() = runTest {
    fetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = flowOf(Result.success(AccountMediaDetailsFactory.NotRated())),
    )

    getMovieDetailsUseCase.mockFetchMediaDetails(
      response = flowOf(
        Result.success(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = MediaDetailsFactory.FightClub(),
            ratingSource = RatingSource.TMDB,
          ),
        ),
      ),
    )

    val allRatingsChannel = Channel<Result<Pair<RatingSource, RatingDetails>>>()

    fetchAllRatingsUseCase.mockSuccess(allRatingsChannel)

    val viewModel = DetailsViewModel(
      getMediaDetailsUseCase = getMovieDetailsUseCase.mock,
      onMarkAsFavoriteUseCase = markAsFavoriteUseCase,
      submitRatingUseCase = submitRateUseCase.mock,
      deleteRatingUseCase = deleteRatingUseCase.mock,
      addToWatchlistUseCase = addToWatchlistUseCase.mock,
      requestMediaUseCase = requestMediaUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      fetchAllRatingsUseCase = fetchAllRatingsUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to 0,
          "isFavorite" to false,
          "mediaType" to MediaType.MOVIE,
        ),
      ),
    )

    setContentWithTheme {
      DetailsScreen(
        onNavigateUp = {},
        onNavigateToDetails = {},
        onNavigateToPerson = {},
        onNavigateToAccountSettings = {},
        onNavigateToCredits = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Rating.DETAILS_RATING_BUTTON).assertIsDisplayed().performClick()

      onNodeWithTag(TestTags.Rating.ALL_RATINGS_BOTTOM_SHEET).assertIsDisplayed()

      onNodeWithTag(
        TestTags.Rating.RATING_SOURCE_SKELETON.format(RatingSource.IMDB),
      ).assertIsDisplayed()

      onNodeWithTag(
        TestTags.Rating.RATING_SOURCE_SKELETON.format(RatingSource.TRAKT),
      ).assertIsDisplayed()

      allRatingsChannel.send(
        Result.success(RatingSource.IMDB to RatingDetails.Score(8.1, 1234)),
      )

      onNodeWithTag(
        TestTags.Rating.RATING_SOURCE_SKELETON.format(RatingSource.IMDB),
      ).assertIsNotDisplayed()

      allRatingsChannel.send(
        Result.success(RatingSource.TRAKT to RatingDetails.Score(8.5, 12345)),
      )

      onNodeWithTag(
        TestTags.Rating.RATING_SOURCE_SKELETON.format(RatingSource.TRAKT),
      ).assertIsNotDisplayed()
    }
  }
}
