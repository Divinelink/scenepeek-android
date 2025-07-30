package com.divinelink.feature.user.data

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.model.data.UserDataResponseFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestFetchUserDataUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import com.divinelink.core.ui.TestTags
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import java.net.UnknownHostException
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class UserDataScreenTest : ComposeTest() {

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchWatchlistUseCase = TestFetchUserDataUseCase()

  @Test
  fun `test unknown error`() = runTest {
    observeAccountUseCase.mockResponse(response = Result.success(true))

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
        ),
      )
    }

    with(composeTestRule) {
      waitForIdle()
      delay(2000)

      onNodeWithTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT).assertIsDisplayed()
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(uiR.string.core_ui_error_generic_title)).assertIsDisplayed()
      onNodeWithText(getString(uiR.string.core_ui_error_generic_description)).assertIsDisplayed()
    }
  }

  @Test
  fun `test network error`() {
    observeAccountUseCase.mockResponse(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(Result.failure(AppException.Offline())),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
        ),
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT).assertIsDisplayed()
    composeTestRule.onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    composeTestRule.onNodeWithText(getString(uiR.string.core_ui_offline_title))
      .assertIsDisplayed()
    composeTestRule.onNodeWithText(getString(uiR.string.core_ui_offline_description))
      .assertIsDisplayed()
  }

  @Test
  fun `test unauthenticated error`() = runTest {
    var verifyNavigatedToTMDBLogin = false

    observeAccountUseCase.mockResponse(
      response = Result.failure(SessionException.Unauthenticated()),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {
          verifyNavigatedToTMDBLogin = true
        },
        onNavigateToMediaDetails = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
        ),
      )
    }

    val loginButton = composeTestRule.activity.getString(
      uiR.string.core_ui_login,
    )

    val loginToSeeWatchlistString = composeTestRule.activity.getString(
      R.string.feature_user_data_login_watchlist_description,
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
    observeAccountUseCase.mockResponse(response = Result.success(true))

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
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
    observeAccountUseCase.mockResponse(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(UserDataResponseFactory.emptyMovies()),
        Result.success(UserDataResponseFactory.emptyTV()),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
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
      R.string.feature_user_data_empty_movies_watchlist,
    )

    composeTestRule.onNodeWithText(emptyMovieWatchlistString).assertIsDisplayed()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .performClick()

    val emptyTVWatchlistString = composeTestRule.activity.getString(
      R.string.feature_user_data_empty_tv_shows_watchlist,
    )

    composeTestRule.onNodeWithText(emptyTVWatchlistString).assertIsDisplayed()
  }

  @Test
  fun `test tv watching is loading`() {
    observeAccountUseCase.mockResponse(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = Result.success(UserDataResponseFactory.emptyMovies()),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
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
      R.string.feature_user_data_empty_movies_watchlist,
    )

    composeTestRule.onNodeWithText(emptyMovieWatchlistString).assertIsDisplayed()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .performClick()

    composeTestRule.onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
  }

  @Test
  fun `test watchlist with movies and tv content`() {
    observeAccountUseCase.mockResponse(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(UserDataResponseFactory.movies()),
        Result.success(UserDataResponseFactory.tv()),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
        ),
      )
    }

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.MOVIE.value))
      .assertIsDisplayed()
      .assertIsSelected()

    composeTestRule.onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaType.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    val movieList = UserDataResponseFactory.movies().data

    composeTestRule.onNodeWithText(movieList.first().name).assertIsDisplayed()
    composeTestRule.onNodeWithText(movieList.last().name).assertDoesNotExist()

    // Scroll to the end of the list
    composeTestRule.onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT)
      .performScrollToNode(
        matcher = hasText(text = movieList.last().name),
      )

    composeTestRule.onNodeWithText(movieList.last().name).assertIsDisplayed()
    composeTestRule.onNodeWithTag(TestTags.SCROLL_TO_TOP_BUTTON).assertIsNotDisplayed()

    // Scroll up to display the ScrollToTopButton
    composeTestRule.onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT)
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

    observeAccountUseCase.mockResponse(response = Result.success(true))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(UserDataResponseFactory.movies()),
        Result.success(UserDataResponseFactory.tv()),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigateUp = {},
        onNavigateToTMDBLogin = {},
        onNavigateToMediaDetails = {
          verifyNavigatedToMediaDetails = true
          navArgs = it
        },
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "userDataSection" to UserDataSection.Watchlist,
            ),
          ),
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

    val tvList = UserDataResponseFactory.tv().data

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
