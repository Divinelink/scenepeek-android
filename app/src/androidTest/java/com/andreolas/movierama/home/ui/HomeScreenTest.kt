package com.andreolas.movierama.home.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.details.ui.DetailsNavArguments
import com.andreolas.movierama.fakes.FakeDestinationsNavigator
import com.andreolas.movierama.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetPopularMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetSearchMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.components.DETAILS_BUTTON_TAG
import com.andreolas.movierama.ui.components.MOVIE_BOTTOM_SHEET_TAG
import com.andreolas.movierama.ui.components.MOVIE_CARD_ITEM_TAG
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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

    @Test
    fun navigateToDetailsScreenTest() {

        val getPopularMoviesUseCase = FakeGetPopularMoviesUseCase()
        val getSearchMoviesUseCase = FakeGetSearchMoviesUseCase()
        val markAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
        val getFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

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
}
