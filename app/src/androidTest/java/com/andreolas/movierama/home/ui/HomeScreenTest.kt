package com.andreolas.movierama.home.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.andreolas.movierama.R
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.ui.DetailsNavArguments
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.andreolas.movierama.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetPopularMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetSearchMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.components.DETAILS_BUTTON_TAG
import com.andreolas.movierama.ui.components.FILTER_BAR_TEST_TAG
import com.andreolas.movierama.ui.components.MOVIE_BOTTOM_SHEET_TAG
import com.andreolas.movierama.ui.components.MOVIE_CARD_ITEM_TAG
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  private val getPopularMoviesUseCase = FakeGetPopularMoviesUseCase()
  private val getSearchMoviesUseCase = FakeGetSearchMoviesUseCase()
  private val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

  private val popularMovies = (1..10).map {
    PopularMovie(
      id = 0,
      posterPath = "",
      releaseDate = "2022/10/20",
      title = "test title $it",
      rating = "$it",
      overview = "lorem ipsum $it",
      isFavorite = false,
    )
  }.toMutableList()

  @OptIn(ExperimentalTestApi::class)
  @Test
  fun navigateToDetailsScreenTest() = runBlocking {
    val destinationsNavigator = FakeDestinationsNavigator()

    getPopularMoviesUseCase.mockFetchPopularMovies(
      result = flowOf(Result.Success(popularMovies))
    )

    composeTestRule.setContent {
      HomeScreen(
        navigator = destinationsNavigator,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase,
          getSearchMoviesUseCase = getSearchMoviesUseCase,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase,
        )
      )
    }
    composeTestRule
      .onNodeWithTag(MOVIE_CARD_ITEM_TAG)
      .performClick()

    composeTestRule.waitUntilExactlyOneExists(
      matcher = hasTestTag(MOVIE_BOTTOM_SHEET_TAG),
      timeoutMillis = DELAY
    )

    composeTestRule
      .onNodeWithTag(MOVIE_BOTTOM_SHEET_TAG)
      .assertIsDisplayed()

    composeTestRule
      .onNodeWithTag(DETAILS_BUTTON_TAG, useUnmergedTree = true)
      .performClick()

    destinationsNavigator.verifyNavigatedToDirection(
      expectedDirection = DetailsScreenDestination(
        DetailsNavArguments(movieId = 0, isFavorite = false)
      )
    )
  }

  @Test
  fun filtersAreHiddenWhenSearchingTest() {
    val destinationsNavigator = FakeDestinationsNavigator()

    getPopularMoviesUseCase.mockFetchPopularMovies(
      result = flowOf(Result.Success(popularMovies))
    )

    getFavoriteMoviesUseCase.mockFetchFavoriteMovies(
      result = flowOf(Result.Success(popularMovies.filter { it.isFavorite }))
    )

    composeTestRule.setContent {
      HomeScreen(
        navigator = destinationsNavigator,
        viewModel = HomeViewModel(
          getPopularMoviesUseCase = getPopularMoviesUseCase,
          getSearchMoviesUseCase = getSearchMoviesUseCase,
          markAsFavoriteUseCase = markAsFavoriteUseCase,
          getFavoriteMoviesUseCase = getFavoriteMoviesUseCase,
        )
      )
    }

    val toolbarSearch = composeTestRule.activity
      .getString(R.string.toolbar_search)

    val searchContentDescription = composeTestRule.activity
      .getString(R.string.toolbar_search_placeholder)

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

  companion object {
    private const val DELAY = 5000L
  }
}
