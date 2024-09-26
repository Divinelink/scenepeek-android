package com.andreolas.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.andreolas.movierama.fakes.usecase.FakeFetchMultiInfoSearchUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetPopularMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.home.ui.HomeScreen
import com.andreolas.movierama.home.ui.HomeViewModel
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.media.MediaItemFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.ui.components.FILTER_BAR_TEST_TAG
import com.divinelink.core.ui.components.MOVIE_CARD_ITEM_TAG
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class HomeScreenTest : ComposeTest() {

  private val getPopularMoviesUseCase = FakeGetPopularMoviesUseCase()
  private val fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()
  private val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  val testDispatcher = mainDispatcherRule.testDispatcher

  @Test
  fun navigateToDetailsScreenTest() = runTest {
    val destinationsNavigator = FakeDestinationsNavigator()

    getPopularMoviesUseCase.mockFetchPopularMovies(
      response = flowOf(Result.success(MediaItemFactory.MoviesList())),
    )

    composeTestRule.setContent {
      HomeScreen(
        navigator = destinationsNavigator,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
          fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
        ),
      )
    }
    composeTestRule
      .onAllNodesWithTag(MOVIE_CARD_ITEM_TAG)[0]
      .performClick()

    composeTestRule.runOnIdle {
      destinationsNavigator.verifyNavigatedToDirection(
        expectedDirection = DetailsScreenDestination(
          DetailsNavArguments(
            id = 1,
            mediaType = MediaType.MOVIE.value,
            isFavorite = false,
          ),
        ),
      )
    }
  }

  @Test
  fun filtersAreHiddenWhenSearchingTest() {
    val destinationsNavigator = FakeDestinationsNavigator()

    val moviesList = MediaItemFactory.MoviesList()

    getPopularMoviesUseCase.mockFetchPopularMovies(
      response = flowOf(Result.success(moviesList)),
    )

    getFavoriteMoviesUseCase.mockGetFavoriteMovies(
      response = Result.success(
        moviesList.filter { it.isFavorite == true } as List<MediaItem.Media>,
      ),
    )

    composeTestRule.setContent {
      HomeScreen(
        navigator = destinationsNavigator,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
          fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        ),
      )
    }

    val searchContentDescription = getString(uiR.string.core_ui_toolbar_search_placeholder)

    composeTestRule
      .onNodeWithTag(FILTER_BAR_TEST_TAG)
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithContentDescription(searchContentDescription)
      .performTextInput("search")

    composeTestRule
      .onNodeWithTag(FILTER_BAR_TEST_TAG)
      .assertDoesNotExist()
  }
}
