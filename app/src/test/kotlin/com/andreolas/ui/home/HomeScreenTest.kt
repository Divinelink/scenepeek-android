package com.andreolas.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.andreolas.factories.MediaItemFactory
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.R
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.andreolas.movierama.fakes.usecase.FakeFetchMultiInfoSearchUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetPopularMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.home.ui.HomeScreen
import com.andreolas.movierama.home.ui.HomeViewModel
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.ui.components.FILTER_BAR_TEST_TAG
import com.divinelink.core.ui.components.MOVIE_CARD_ITEM_TAG
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.ui.DetailsNavArguments
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
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
      response = flowOf(Result.success(MediaItemFactory.MoviesList()))
    )

    composeTestRule.setContent {
      HomeScreen(
        navigator = destinationsNavigator,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
          fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock
        )
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
          )
        )
      )
    }
  }

  @Test
  fun filtersAreHiddenWhenSearchingTest() {
    val destinationsNavigator = FakeDestinationsNavigator()

    val moviesList = MediaItemFactory.MoviesList()

    getPopularMoviesUseCase.mockFetchPopularMovies(
      response = flowOf(Result.success(moviesList))
    )

    getFavoriteMoviesUseCase.mockGetFavoriteMovies(
      response = Result.success(
        moviesList.filter { it.isFavorite == true } as List<MediaItem.Media>
      )
    )

    composeTestRule.setContent {
      HomeScreen(
        navigator = destinationsNavigator,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase.mock,
          fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase.mock,
        )
      )
    }

    val toolbarSearch = composeTestRule.activity
      .getString(R.string.toolbar_search)

    val searchContentDescription = composeTestRule.activity
      .getString(uiR.string.toolbar_search_placeholder)

    composeTestRule
      .onNodeWithTag(FILTER_BAR_TEST_TAG)
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithText(toolbarSearch)
      .performClick()

    composeTestRule
      .onNodeWithContentDescription(searchContentDescription)
      .performTextInput("search")

    composeTestRule
      .onNodeWithTag(FILTER_BAR_TEST_TAG)
      .assertDoesNotExist()
  }
}
