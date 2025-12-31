package com.divinelink.feature.search

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.components.MOVIE_CARD_ITEM_TAG
import com.divinelink.core.ui.resources.core_ui_offline_description
import com.divinelink.core.ui.resources.core_ui_offline_title
import com.divinelink.feature.search.resources.Res
import com.divinelink.feature.search.resources.feature_search__initial_description
import com.divinelink.feature.search.resources.search__empty_result_description
import com.divinelink.feature.search.resources.search__empty_result_title
import com.divinelink.feature.search.ui.SearchContent
import com.divinelink.feature.search.ui.SearchUiState
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class SearchContentTest : ComposeTest() {

  @Test
  fun `test initial state`() = uiTest {
    val uiState = SearchUiState.initial()

    setContentWithTheme {
      SearchContent(
        onNavigate = {},
        uiState = uiState,
        onLoadNextPage = {},
        onRetryClick = {},
        scrollState = rememberLazyGridState(),
      )
    }

    onNodeWithText(getString(Res.string.feature_search__initial_description)).assertIsDisplayed()
  }

  @Test
  fun `test search content with data`() = uiTest {
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
        onLoadNextPage = {},
        onRetryClick = {},
        scrollState = rememberLazyGridState(),
      )
    }

    onNodeWithTag(TestTags.MEDIA_LIST_TAG).assertIsDisplayed()
  }

  @Test
  fun `test long click on media card`() = uiTest {
    var route: Navigation? = null

    val uiState = SearchUiState.initial().copy(
      searchResults = MediaSection(
        data = MediaItemFactory.MoviesList(),
        shouldLoadMore = false,
      ),
      focusSearch = false,
    )

    setContentWithTheme {
      SearchContent(
        onNavigate = { route = it },
        uiState = uiState,
        onLoadNextPage = {},
        onRetryClick = {},
        scrollState = rememberLazyGridState(),
      )
    }

    onAllNodesWithTag(MOVIE_CARD_ITEM_TAG)[0]
      .performTouchInput {
        longClick()
      }
    route shouldBe Navigation.ActionMenuRoute.Media(
      MediaItemFactory.MoviesList().first().encodeToString(),
    )
  }

  @Test
  fun `test empty search blank slate is displayed on empty search result without other error`() =
    uiTest {
      val uiState = SearchUiState.Companion.initial().copy(
        searchResults = MediaSection(
          data = emptyList(),
          shouldLoadMore = false,
        ),
      )

      setContentWithTheme {
        SearchContent(
          onNavigate = {},
          uiState = uiState,
          onLoadNextPage = {},
          onRetryClick = {},
          scrollState = rememberLazyGridState(),
        )
      }

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(Res.string.search__empty_result_title)).assertIsDisplayed()
      onNodeWithText(
        getString(Res.string.search__empty_result_description),
      ).assertIsDisplayed()
    }

  @Test
  fun `test search blank slate when is empty and search mode but has other error`() = uiTest {
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
        scrollState = rememberLazyGridState(),
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    onNodeWithText(
      getString(UiString.core_ui_offline_title),
    ).assertIsDisplayed()
    onNodeWithText(
      getString(UiString.core_ui_offline_description),
    ).assertIsDisplayed()
    onNodeWithContentDescription("Blank slate illustration").assertIsDisplayed()
  }
}
