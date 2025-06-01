package com.divinelink.feature.watchlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.model.watchlist.WatchlistResponseFactory
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.FakeFetchWatchlistUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import com.divinelink.core.ui.TestTags
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class WatchlistScreenTest : ComposeTest() {

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchWatchlistUseCase = FakeFetchWatchlistUseCase()

  @Test
  fun `test unknown error`() {
    observeAccountUseCase.mockSuccess(response = Result.success(true))

    setVisibilityScopeContent {
      WatchlistScreen(
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = WatchlistViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
        animatedVisibilityScope = this,
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
    var verifyNavigatedToTMDBLogin = false

    observeAccountUseCase.mockSuccess(response = Result.failure(SessionException.Unauthenticated()))

    setVisibilityScopeContent {
      WatchlistScreen(
        onNavigateToTMDBLogin = {
          verifyNavigatedToTMDBLogin = true
        },
        onNavigateToMediaDetails = {},
        viewModel = WatchlistViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
        animatedVisibilityScope = this,
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
    assertThat(verifyNavigatedToTMDBLogin).isTrue()
  }

  @Test
  fun `test watchlist tabs are visible with movies and tv tabs`() {
    observeAccountUseCase.mockSuccess(response = Result.success(true))

    setVisibilityScopeContent {
      WatchlistScreen(
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = WatchlistViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
        animatedVisibilityScope = this,
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
    observeAccountUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(WatchlistResponseFactory.emptyMovies()),
        Result.success(WatchlistResponseFactory.emptyTV()),
      ),
    )

    setVisibilityScopeContent {
      WatchlistScreen(
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = WatchlistViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
        animatedVisibilityScope = this,
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
    observeAccountUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = Result.success(WatchlistResponseFactory.emptyMovies()),
    )

    setVisibilityScopeContent {
      WatchlistScreen(
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = WatchlistViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
        animatedVisibilityScope = this,
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

    composeTestRule.onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
  }

  @Test
  fun `test watchlist with movies and tv content`() {
    observeAccountUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(WatchlistResponseFactory.movies()),
        Result.success(WatchlistResponseFactory.tv()),
      ),
    )

    setVisibilityScopeContent {
      WatchlistScreen(
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = WatchlistViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
        animatedVisibilityScope = this,
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
    composeTestRule.onNodeWithTag(TestTags.SCROLL_TO_TOP_BUTTON).assertIsNotDisplayed()

    // Scroll up to display the ScrollToTopButton
    composeTestRule.onNodeWithTag(TestTags.Watchlist.WATCHLIST_CONTENT)
      .performScrollToNode(
        matcher = hasText(text = movieList[movieList.lastIndex - 1].name),
      )

    composeTestRule.onNodeWithTag(TestTags.SCROLL_TO_TOP_BUTTON).assertIsDisplayed().performClick()

    // Should be at the top of the list
    composeTestRule.onNodeWithText(movieList.first().name).assertIsDisplayed()
    composeTestRule.onNodeWithText(movieList.last().name).assertDoesNotExist()
  }

  @Test
  fun `test nagivate to details from tv content`() {
    var verifyNavigatedToMediaDetails = false
    var navArgs: DetailsRoute? = null

    observeAccountUseCase.mockSuccess(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(WatchlistResponseFactory.movies()),
        Result.success(WatchlistResponseFactory.tv()),
      ),
    )

    setVisibilityScopeContent {
      WatchlistScreen(
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {
          verifyNavigatedToMediaDetails = true
          navArgs = it
        },
        viewModel = WatchlistViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
        ),
        animatedVisibilityScope = this,
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

    assertThat(verifyNavigatedToMediaDetails).isTrue()
    assertThat(navArgs).isEqualTo(
      DetailsRoute(
        mediaType = MediaType.TV,
        id = tvList.first().id,
        isFavorite = tvList.first().isFavorite,
      ),
    )
  }
}
