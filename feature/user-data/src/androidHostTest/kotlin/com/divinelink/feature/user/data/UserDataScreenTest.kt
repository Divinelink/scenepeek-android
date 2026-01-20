package com.divinelink.feature.user.data

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.model.tab.MediaTab
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.model.data.UserDataResponseFactory
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.TestFetchUserDataUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_error_generic_description
import com.divinelink.core.ui.resources.core_ui_error_generic_title
import com.divinelink.core.ui.resources.core_ui_login
import com.divinelink.core.ui.resources.core_ui_offline_description
import com.divinelink.core.ui.resources.core_ui_offline_title
import com.divinelink.feature.user.data.resources.Res
import com.divinelink.feature.user.data.resources.feature_user_data_empty_watchlist
import com.divinelink.feature.user.data.resources.feature_user_data_login_watchlist_description
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class UserDataScreenTest : ComposeTest() {

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchWatchlistUseCase = TestFetchUserDataUseCase()

  private val preferencesRepository = TestPreferencesRepository()
  private val switchViewButtonViewModel = SwitchViewButtonViewModel(
    repository = preferencesRepository,
  )

  @Test
  fun `test unknown error`() = uiTest {
    observeAccountUseCase.mockResponse(
      Result.success(TMDBAccountFactory.loggedIn()),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {},
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "section" to UserDataSection.Watchlist.value,
            ),
          ),
        ),
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    waitForIdle()
    delay(2000)

    onNodeWithTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT).assertIsDisplayed()
    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_error_generic_title)).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_error_generic_description)).assertIsDisplayed()
  }

  @Test
  fun `test network error`() = uiTest {
    observeAccountUseCase.mockResponse(response = Result.success(TMDBAccountFactory.loggedIn()))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(Result.failure(AppException.Offline())),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {},
        switchViewButtonViewModel = switchViewButtonViewModel,
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "section" to UserDataSection.Watchlist.value,
            ),
          ),
        ),
      )
    }

    onNodeWithTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT).assertIsDisplayed()
    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_offline_title))
      .assertIsDisplayed()
    onNodeWithText(getString(UiString.core_ui_offline_description))
      .assertIsDisplayed()
  }

  @Test
  fun `test unauthenticated error`() = uiTest {
    var verifyNavigatedToTMDBLogin = false

    observeAccountUseCase.mockResponse(
      response = Result.failure(SessionException.Unauthenticated()),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {
          if (it is Navigation.TMDBAuthRoute) {
            verifyNavigatedToTMDBLogin = true
          }
        },
        switchViewButtonViewModel = switchViewButtonViewModel,
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "section" to UserDataSection.Watchlist.value,
            ),
          ),
        ),
      )
    }

    val loginButton = getString(UiString.core_ui_login)

    val loginToSeeWatchlistString = getString(
      Res.string.feature_user_data_login_watchlist_description,
    )

    onNodeWithTag(TestTags.Watchlist.WATCHLIST_ERROR_CONTENT).assertIsDisplayed()
    onNodeWithText(loginToSeeWatchlistString).assertIsDisplayed()
    onNodeWithText(loginButton).assertIsDisplayed()

    // Navigate to Login
    onNodeWithText(loginButton).performClick()
    assertThat(verifyNavigatedToTMDBLogin).isTrue()
  }

  @Test
  fun `test watchlist tabs are visible with movies and tv tabs`() = uiTest {
    observeAccountUseCase.mockResponse(response = Result.success(TMDBAccountFactory.loggedIn()))

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {},
        switchViewButtonViewModel = switchViewButtonViewModel,
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "section" to UserDataSection.Watchlist.value,
            ),
          ),
        ),

      )
    }

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.Movie.value))
      .assertIsDisplayed()
      .assertIsSelected()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .performClick()
      .assertIsSelected()
  }

  @Test
  fun `test watchlist with empty content`() = uiTest {
    observeAccountUseCase.mockResponse(response = Result.success(TMDBAccountFactory.loggedIn()))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(UserDataResponseFactory.emptyMovies()),
        Result.success(UserDataResponseFactory.emptyTV()),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {},
        switchViewButtonViewModel = switchViewButtonViewModel,
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "section" to UserDataSection.Watchlist.value,
            ),
          ),
        ),
      )
    }

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.Movie.value))
      .assertIsDisplayed()
      .assertIsSelected()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    onNodeWithText("Your watchlist is empty").assertIsDisplayed()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .performClick()

    onNodeWithText("Your watchlist is empty").assertIsDisplayed()
  }

  @Test
  fun `test tv watching is loading`() = uiTest {
    observeAccountUseCase.mockResponse(response = Result.success(TMDBAccountFactory.loggedIn()))
    fetchWatchlistUseCase.mockSuccess(
      response = Result.success(UserDataResponseFactory.emptyMovies()),
    )

    val viewModel = UserDataViewModel(
      observeAccountUseCase = observeAccountUseCase.mock,
      fetchUserDataUseCase = fetchWatchlistUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "section" to UserDataSection.Watchlist.value,
        ),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {},
        viewModel = viewModel,
        switchViewButtonViewModel = switchViewButtonViewModel,
      )
    }

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.Movie.value))
      .assertIsDisplayed()
      .assertIsSelected()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    val emptyMovieWatchlistString = getString(
      Res.string.feature_user_data_empty_watchlist,
    )

    onNodeWithText(emptyMovieWatchlistString).assertIsDisplayed()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .performClick()

    onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
  }

  @Test
  fun `test watchlist with movies and tv content`() = uiTest {
    observeAccountUseCase.mockResponse(response = Result.success(TMDBAccountFactory.loggedIn()))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(UserDataResponseFactory.movies()),
        Result.success(UserDataResponseFactory.tv()),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {},
        switchViewButtonViewModel = switchViewButtonViewModel,
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "section" to UserDataSection.Watchlist.value,
            ),
          ),
        ),
      )
    }

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.Movie.value))
      .assertIsDisplayed()
      .assertIsSelected()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()

    val movieList = UserDataResponseFactory.movies().data

    onNodeWithText(movieList.first().name).assertIsDisplayed()
    onNodeWithText(movieList.last().name).assertDoesNotExist()

    // Scroll to the end of the list
    onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT)
      .performScrollToNode(
        matcher = hasText(text = movieList.last().name),
      )

    onNodeWithText(movieList.last().name).assertIsDisplayed()
    onNodeWithTag(TestTags.SCROLL_TO_TOP_BUTTON).assertIsNotDisplayed()

    // Scroll up to display the ScrollToTopButton
    onNodeWithTag(TestTags.Components.MEDIA_LIST_CONTENT)
      .performTouchInput {
        val center = centerY

        swipeDown(
          startY = center,
          endY = center + 100,
        )
      }

    onNodeWithTag(TestTags.SCROLL_TO_TOP_BUTTON).assertIsDisplayed().performClick()

    // Should be at the top of the list
    onNodeWithText(movieList.first().name).assertIsDisplayed()
    onNodeWithText(movieList.last().name).assertDoesNotExist()
  }

  @Test
  fun `test navigate to details from tv content`() = uiTest {
    var verifyNavigatedToMediaDetails = false
    var navArgs: Navigation.DetailsRoute? = null

    observeAccountUseCase.mockResponse(response = Result.success(TMDBAccountFactory.loggedIn()))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(UserDataResponseFactory.movies()),
        Result.success(UserDataResponseFactory.tv()),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = {
          if (it is Navigation.DetailsRoute) {
            verifyNavigatedToMediaDetails = true
            navArgs = it
          }
        },
        switchViewButtonViewModel = switchViewButtonViewModel,
        viewModel = UserDataViewModel(
          observeAccountUseCase = observeAccountUseCase.mock,
          fetchUserDataUseCase = fetchWatchlistUseCase.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "section" to UserDataSection.Watchlist.value,
            ),
          ),
        ),
      )
    }

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.Movie.value))
      .assertIsDisplayed()
      .assertIsSelected()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()
      .performClick()
      .assertIsSelected()

    val tvList = UserDataResponseFactory.tv().data

    onNodeWithText(tvList.first().name).assertIsDisplayed()
    onNodeWithText(tvList.last().name).assertDoesNotExist()

    onNodeWithText(tvList.first().name).performClick()

    assertThat(verifyNavigatedToMediaDetails).isTrue()
    assertThat(navArgs).isEqualTo(
      Navigation.DetailsRoute(
        mediaType = MediaType.TV.value,
        id = tvList.first().id,
        isFavorite = tvList.first().isFavorite,
      ),
    )
  }

  @Test
  fun `test open action modal on long press`() = uiTest {
    var route: Navigation? = null

    observeAccountUseCase.mockResponse(response = Result.success(TMDBAccountFactory.loggedIn()))
    fetchWatchlistUseCase.mockSuccess(
      response = flowOf(
        Result.success(UserDataResponseFactory.movies()),
        Result.success(UserDataResponseFactory.tv()),
      ),
    )

    val viewModel = UserDataViewModel(
      observeAccountUseCase = observeAccountUseCase.mock,
      fetchUserDataUseCase = fetchWatchlistUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "section" to UserDataSection.Watchlist.value,
        ),
      ),
    )

    setVisibilityScopeContent {
      UserDataScreen(
        onNavigate = { route = it },
        switchViewButtonViewModel = switchViewButtonViewModel,
        viewModel = viewModel,
      )
    }

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.Movie.value))
      .assertIsDisplayed()
      .assertIsSelected()

    onNodeWithTag(TestTags.Watchlist.TAB_BAR.format(MediaTab.TV.value))
      .assertIsDisplayed()
      .assertIsNotSelected()
      .performClick()
      .assertIsSelected()

    val tvList = UserDataResponseFactory.tv().data

    onNodeWithText(tvList.first().name).assertIsDisplayed()
    onNodeWithText(tvList.last().name).assertDoesNotExist()

    onNodeWithText(tvList.first().name).performTouchInput { longClick() }

    assertThat(route).isEqualTo(
      Navigation.ActionMenuRoute.Media(tvList.first().encodeToString()),
    )
  }
}
