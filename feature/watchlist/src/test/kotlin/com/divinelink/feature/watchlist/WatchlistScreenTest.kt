package com.divinelink.feature.watchlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.model.watchlist.WatchlistResponseFactory
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeFetchWatchlistUseCase
import com.divinelink.core.testing.usecase.FakeObserveSessionUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import com.divinelink.core.ui.R as uiR

class WatchlistScreenTest : ComposeTest() {

  private val observeSessionUseCase = FakeObserveSessionUseCase()
  private val fetchWatchlistUseCase = FakeFetchWatchlistUseCase()

  @Test
  fun `test unknown error`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    observeSessionUseCase.mockSuccess(response = Result.success(true))

    setContentWithTheme {
      WatchlistScreen(
        navigator = destinationsNavigator,
        viewModel = WatchlistViewModel(
          observeSessionUseCase = observeSessionUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
      )
    }

    val generalErrorString = composeTestRule.activity.getString(
      uiR.string.core_ui_error_retry,
    )

    composeTestRule.onNodeWithTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT).assertIsDisplayed()
    composeTestRule.onNodeWithText(generalErrorString).assertIsDisplayed()
  }

  @Test
  fun `test unauthenticated error`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    observeSessionUseCase.mockSuccess(response = Result.failure(SessionException.Unauthenticated()))

    setContentWithTheme {
      WatchlistScreen(
        navigator = destinationsNavigator,
        viewModel = WatchlistViewModel(
          observeSessionUseCase = observeSessionUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
      )
    }

    val loginButton = composeTestRule.activity.getString(
      uiR.string.core_ui_login,
    )

    val loginToSeeWatchlistString = composeTestRule.activity.getString(
      R.string.feature_watchlist_login_to_see_watchlist,
    )

    composeTestRule.onNodeWithTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT).assertIsDisplayed()
    composeTestRule.onNodeWithText(loginToSeeWatchlistString).assertIsDisplayed()
    composeTestRule.onNodeWithText(loginButton).assertIsDisplayed()

    // Navigate to Login
    composeTestRule.onNodeWithText(loginButton).performClick()
    destinationsNavigator.verifyNavigatedToDirection(AccountSettingsScreenDestination())
  }

  @Test
  fun `test watchlist tabs are visible with movies and tv tabs`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    observeSessionUseCase.mockSuccess(response = Result.success(true))

    setContentWithTheme {
      WatchlistScreen(
        navigator = destinationsNavigator,
        viewModel = WatchlistViewModel(
          observeSessionUseCase = observeSessionUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.MOVIE.value))
      .assertIsDisplayed()
      .assertIsSelected()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .performClick()
      .assertIsSelected()
  }

  @Test
  fun `test watchlist with empty content`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    observeSessionUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(WatchlistResponseFactory.emptyMovies()),
        Result.success(WatchlistResponseFactory.emptyTV()),
      ),
    )

    setContentWithTheme {
      WatchlistScreen(
        navigator = destinationsNavigator,
        viewModel = WatchlistViewModel(
          observeSessionUseCase = observeSessionUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.MOVIE.value))
      .assertIsDisplayed()
      .assertIsSelected()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    val emptyMovieWatchlistString = composeTestRule.activity.getString(
      R.string.feature_watchlist_empty_movies_watchlist,
    )

    composeTestRule.onNodeWithText(emptyMovieWatchlistString).assertIsDisplayed()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .performClick()

    val emptyTVWatchlistString = composeTestRule.activity.getString(
      R.string.feature_watchlist_empty_tv_shows_watchlist,
    )

    composeTestRule.onNodeWithText(emptyTVWatchlistString).assertIsDisplayed()
  }

  @Test
  fun `test tv watching is loading`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    observeSessionUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = Result.success(WatchlistResponseFactory.emptyMovies()),
    )

    setContentWithTheme {
      WatchlistScreen(
        navigator = destinationsNavigator,
        viewModel = WatchlistViewModel(
          observeSessionUseCase = observeSessionUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.MOVIE.value))
      .assertIsDisplayed()
      .assertIsSelected()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    val emptyMovieWatchlistString = composeTestRule.activity.getString(
      R.string.feature_watchlist_empty_movies_watchlist,
    )

    composeTestRule.onNodeWithText(emptyMovieWatchlistString).assertIsDisplayed()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .performClick()

    composeTestRule.onNodeWithTag(TestTags.LOADING_CONTENT_TAG).assertIsDisplayed()
  }

  @Test
  fun `test watchlist with movies and tv content`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    observeSessionUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(WatchlistResponseFactory.movies()),
        Result.success(WatchlistResponseFactory.tv()),
      ),
    )

    setContentWithTheme {
      WatchlistScreen(
        navigator = destinationsNavigator,
        viewModel = WatchlistViewModel(
          observeSessionUseCase = observeSessionUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.MOVIE.value))
      .assertIsDisplayed()
      .assertIsSelected()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    val movieList = WatchlistResponseFactory.movies().data

    composeTestRule.onNodeWithText(movieList.first().name).assertIsDisplayed()
    composeTestRule.onNodeWithText(movieList.last().name).assertDoesNotExist()

    // Scroll to the end of the list
    composeTestRule.onNodeWithTag(TestTags.Watchlist.WATCHLIST_CONTENT)
      .performScrollToNode(
        matcher = hasText(text = movieList.last().name),
      )

    composeTestRule.onNodeWithText(movieList.last().name).assertIsDisplayed()
    composeTestRule.onNodeWithTag(TestTags.SCROLL_TO_TOP_BUTTON).assertIsDisplayed().performClick()

    // Should be at the top of the list
    composeTestRule.onNodeWithText(movieList.first().name).assertIsDisplayed()
    composeTestRule.onNodeWithText(movieList.last().name).assertDoesNotExist()
  }

  @Test
  fun `test nagivate to details from tv content`() {
    val destinationsNavigator = FakeDestinationsNavigator()

    observeSessionUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(WatchlistResponseFactory.movies()),
        Result.success(WatchlistResponseFactory.tv()),
      ),
    )

    setContentWithTheme {
      WatchlistScreen(
        navigator = destinationsNavigator,
        viewModel = WatchlistViewModel(
          observeSessionUseCase = observeSessionUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.MOVIE.value))
      .assertIsDisplayed()
      .assertIsSelected()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()
      .performClick()
      .assertIsSelected()

    val tvList = WatchlistResponseFactory.tv().data

    composeTestRule.onNodeWithText(tvList.first().name).assertIsDisplayed()
    composeTestRule.onNodeWithText(tvList.last().name).assertDoesNotExist()

    composeTestRule.onNodeWithText(tvList.first().name).performClick()

    destinationsNavigator.verifyNavigatedToDirection(
      DetailsScreenDestination(
        mediaType = MediaType.TV.value,
        id = tvList.first().id,
        isFavorite = tvList.first().isFavorite,
      ),
    )
  }
}
