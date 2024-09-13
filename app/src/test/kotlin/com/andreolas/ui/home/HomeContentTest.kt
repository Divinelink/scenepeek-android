package com.andreolas.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andreolas.movierama.R
import com.andreolas.movierama.home.ui.HomeContent
import com.andreolas.movierama.home.ui.HomeViewState
import com.andreolas.movierama.home.ui.MediaSection
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class HomeContentTest : ComposeTest() {

  @Test
  fun `test initial loading`() {
    val uiState = HomeViewState.initial()

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onMarkAsFavoriteClicked = {},
        onSearchMovies = {},
        onClearClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = { },
        onNavigateToSettings = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertExists()
    }
  }

  @Test
  fun `test blank slate is visible when ui state has blank slate state`() {
    val uiState = HomeViewState.initial().copy(
      error = BlankSlateState.Offline,
      retryAction = HomeMode.Browser,
    )

    var onRetryClicked = false

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onMarkAsFavoriteClicked = {},
        onSearchMovies = {},
        onClearClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = { onRetryClicked = true },
        onNavigateToSettings = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertExists()
      onNodeWithText(getString(uiR.string.core_ui_retry)).assertExists()
      onNodeWithContentDescription("Blank slate illustration").assertExists()

      onNodeWithText(getString(uiR.string.core_ui_retry)).performClick()
      assertThat(onRetryClicked).isTrue()
    }
  }

  @Test
  fun `test blank slate retry is not visible when retry is null`() {
    val uiState = HomeViewState.initial().copy(
      error = BlankSlateState.Offline,
      retryAction = null,
    )

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onMarkAsFavoriteClicked = {},
        onSearchMovies = {},
        onClearClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = { },
        onNavigateToSettings = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertExists()
      onNodeWithText(getString(uiR.string.core_ui_retry)).assertDoesNotExist()
    }
  }

  @Test
  fun `test blank slate is not visible when error state is null`() {
    val uiState = HomeViewState.initial().copy(
      error = null,
      retryAction = null,
    )

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onMarkAsFavoriteClicked = {},
        onSearchMovies = {},
        onClearClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = { },
        onNavigateToSettings = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertDoesNotExist()
    }
  }

  @Test
  fun `test empty filter blank slate when is empty and filter mode`() {
    val uiState = HomeViewState.initial().copy(
      error = null,
      retryAction = null,
      filteredResults = MediaSection(
        data = emptyList(),
        shouldLoadMore = false,
      ),
      mode = HomeMode.Filtered,
    )

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onMarkAsFavoriteClicked = {},
        onSearchMovies = {},
        onClearClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = { },
        onNavigateToSettings = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(R.string.home__empty_filtered_result_title)).assertIsDisplayed()
      onNodeWithText(
        getString(R.string.home__empty_filtered_result_description),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test empty search blank slate when is empty and search mode without other error`() {
    val uiState = HomeViewState.initial().copy(
      error = null,
      retryAction = null,
      filteredResults = MediaSection(
        data = emptyList(),
        shouldLoadMore = false,
      ),
      searchResults = MediaSection(
        data = emptyList(),
        shouldLoadMore = false,
      ),
      mode = HomeMode.Search,
    )

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onMarkAsFavoriteClicked = {},
        onSearchMovies = {},
        onClearClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = { },
        onNavigateToSettings = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(R.string.search__empty_result_title)).assertIsDisplayed()
      onNodeWithText(
        getString(R.string.search__empty_result_description),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test search blank slate when is empty and search mode but has other error`() {
    val uiState = HomeViewState.initial().copy(
      error = BlankSlateState.Offline,
      retryAction = null,
      filteredResults = MediaSection(
        data = emptyList(),
        shouldLoadMore = false,
      ),
      searchResults = MediaSection(
        data = emptyList(),
        shouldLoadMore = false,
      ),
      mode = HomeMode.Search,
    )

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onMarkAsFavoriteClicked = {},
        onSearchMovies = {},
        onClearClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = { },
        onNavigateToSettings = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(uiR.string.core_ui_offline_title)).assertIsDisplayed()
      onNodeWithText(getString(uiR.string.core_ui_offline_description)).assertIsDisplayed()
      onNodeWithContentDescription("Blank slate illustration").assertIsDisplayed()
    }
  }
}
