package com.divinelink.feature.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.TestTags.MEDIA_LIST_TAG
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.search.ui.SearchContent
import com.divinelink.feature.search.ui.SearchUiState
import kotlin.test.Test

class SearchContentTest : ComposeTest() {

  @Test
  fun `test initial state`() {
    val uiState = SearchUiState.initial()

    setContentWithTheme {
      SearchContent(
        onNavigate = {},
        uiState = uiState,
        onMarkAsFavorite = {},
        onLoadNextPage = {},
        onRetryClick = {},
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.feature_search__initial_description)).assertIsDisplayed()
    }
  }

  @Test
  fun `test search content with data`() {
    val uiState = SearchUiState.initial().copy(
      searchResults = MediaSection(
        data = MediaItemFactory.MoviesList(),
        shouldLoadMore = false,
      ),
      focusSearch = false,
    )

    setContentWithTheme {
      SearchContent(
        onNavigate = {},
        uiState = uiState,
        onMarkAsFavorite = {},
        onLoadNextPage = {},
        onRetryClick = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(MEDIA_LIST_TAG).assertIsDisplayed()
    }
  }

  @Test
  fun `test empty search blank slate is displayed on empty search result without other error`() {
    val uiState = SearchUiState.initial().copy(
      searchResults = MediaSection(
        data = emptyList(),
        shouldLoadMore = false,
      ),
    )

    setContentWithTheme {
      SearchContent(
        onNavigate = {},
        uiState = uiState,
        onMarkAsFavorite = {},
        onLoadNextPage = {},
        onRetryClick = {},
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
    val uiState = SearchUiState.initial().copy(
      error = BlankSlateState.Offline,
      searchResults = MediaSection(
        data = emptyList(),
        shouldLoadMore = false,
      ),
    )

    setContentWithTheme {
      SearchContent(
        uiState = uiState,
        onNavigate = {},
        onLoadNextPage = {},
        onRetryClick = {},
        onMarkAsFavorite = {},
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(
        getString(com.divinelink.core.ui.R.string.core_ui_offline_title),
      ).assertIsDisplayed()
      onNodeWithText(
        getString(com.divinelink.core.ui.R.string.core_ui_offline_description),
      ).assertIsDisplayed()
      onNodeWithContentDescription("Blank slate illustration").assertIsDisplayed()
    }
  }
}
