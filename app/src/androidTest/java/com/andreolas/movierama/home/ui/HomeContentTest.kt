package com.andreolas.movierama.home.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.andreolas.movierama.R
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.components.BOTTOM_SHEET_MARK_AS_FAVORITE
import com.andreolas.movierama.ui.components.MOVIE_CARD_ITEM_TAG
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class HomeContentTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val popularMovies = (1..10).map {
        PopularMovie(
            id = it,
            posterPath = "",
            releaseDate = "2022/10/20",
            title = "test title $it",
            rating = "$it",
            overview = "lorem ipsum $it",
            isFavorite = false,
        )
    }.toMutableList()

    @Test
    fun clickMarkAsFavoriteButton() {
        var hasClickedMarkAsFavorite = false
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = popularMovies,
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {
                    hasClickedMarkAsFavorite = true
                },
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        val markAsFavoriteContentDescription = composeTestRule.activity
            .getString(R.string.mark_as_favorite_button_content_description)

        composeTestRule
            .onAllNodesWithContentDescription(markAsFavoriteContentDescription)[0]
            .performClick()

        assertThat(hasClickedMarkAsFavorite).isTrue()
    }

    @Test
    fun clickClearSearchButton() {
        var hasClickedClearSearchButton = false
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = popularMovies,
                    query = "movie",
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {
                    hasClickedClearSearchButton = true
                },
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        val clearSearchContentDescription = composeTestRule.activity
            .getString(R.string.clear_search_button_content_description)

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .performClick()

        assertThat(hasClickedClearSearchButton).isTrue()
    }

    @Test
    fun clearButtonIsNotVisibleWhenQueryIsEmpty() {
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = popularMovies,
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        val clearSearchContentDescription = composeTestRule.activity
            .getString(R.string.clear_search_button_content_description)

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .assertDoesNotExist()
    }

    @Test
    fun clickOnMovieItem() {
        var hasClickedOnMovie = false
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = popularMovies,
                ),
                onMovieClicked = {
                    hasClickedOnMovie = true
                },
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        composeTestRule
            .onAllNodesWithTag(MOVIE_CARD_ITEM_TAG)[0]
            .performClick()

        assertThat(hasClickedOnMovie).isTrue()
    }

    @Test
    fun renderWithNoMovies() {
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = emptyList(),
                    emptyResult = true,
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        val emptyMoviesLabel = composeTestRule.activity
            .getString(R.string.search__empty_result_title)

        composeTestRule
            .onNodeWithText(emptyMoviesLabel)
            .assertIsDisplayed()
    }

    @Test
    fun initialLoadingTest() {
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = true,
                    moviesList = emptyList(),
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        composeTestRule
            .onNodeWithTag(LOADING_CONTENT_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun renderLoadingMoreTest() {
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = true,
                    moviesList = popularMovies,
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        val loadingMoreLabel = composeTestRule.activity
            .getString(R.string.load_more)

        composeTestRule
            .onNodeWithTag(MOVIES_LIST_TAG)
            .performScrollToIndex(10)

        composeTestRule
            .onNodeWithText(loadingMoreLabel)
            .assertIsDisplayed()
    }

    @Test
    fun focusOnSearchBarTest() {
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = popularMovies,
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        val toolbarSearch = composeTestRule.activity
            .getString(R.string.toolbar_search)

        val toolbarSearchPlaceholder = composeTestRule.activity
            .getString(R.string.toolbar_search_placeholder)

        composeTestRule
            .onNodeWithText(toolbarSearch)
            .performClick()

        composeTestRule
            .onNodeWithText(toolbarSearchPlaceholder)
            .assertIsFocused()
    }

    @Test
    fun clickMarkAsFavoriteFromBottomSheetTest() {
        var markAsFavoriteClicked = false
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = popularMovies,
                    selectedMovie = popularMovies[2],
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {
                    markAsFavoriteClicked = true
                },
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }
        composeTestRule
            .onNodeWithTag(BOTTOM_SHEET_MARK_AS_FAVORITE)
            .performClick()

        assertThat(markAsFavoriteClicked).isTrue()
    }

    @Test
    fun clickOnSettingsButtonTest() {
        composeTestRule.setContent {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = popularMovies,
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onSearchMovies = {},
                onClearClicked = {},
                onLoadNextPage = {},
                onGoToDetails = {},
            )
        }

        val settingsButtonContentDescription = composeTestRule.activity
            .getString(R.string.settings_button_content_description)

        composeTestRule
            .onNodeWithContentDescription(settingsButtonContentDescription)
            .assertIsDisplayed()
            .performClick()
    }
}
