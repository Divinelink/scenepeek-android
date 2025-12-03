package com.divinelink.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.resources.core_ui_retry
import com.divinelink.scenepeek.home.ui.HomeContent
import com.divinelink.scenepeek.home.ui.HomeViewState
import com.divinelink.scenepeek.resources.Res
import com.divinelink.scenepeek.resources.home__empty_filtered_result_description
import com.divinelink.scenepeek.resources.home__empty_filtered_result_title
import com.google.common.truth.Truth.assertThat
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class HomeContentTest : ComposeTest() {

  @Test
  fun `test initial loading`() = uiTest {
    val uiState = HomeViewState.initial()

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onNavigate = {},
        onRetryClick = {},
      )
    }

    onNodeWithTag(TestTags.LOADING_CONTENT).assertExists()
  }

  @Test
  fun `test blank slate is visible when ui state has blank slate state`() = uiTest {
    val uiState = HomeViewState.initial().copy(
      error = BlankSlateState.Offline,
      retryAction = HomeMode.Browser,
    )

    var onRetryClicked = false

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onNavigate = {},
        onRetryClick = { onRetryClicked = true },
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertExists()
    onNodeWithText(getString(UiString.core_ui_retry)).assertExists()
    onNodeWithContentDescription("Blank slate illustration").assertExists()

    onNodeWithText(getString(UiString.core_ui_retry)).performClick()
    assertThat(onRetryClicked).isTrue()
  }

  @Test
  fun `test blank slate retry is not visible when retryText is null`() = uiTest {
    val uiState = HomeViewState.initial().copy(
      error = BlankSlateState.Unauthenticated(),
      retryAction = null,
    )

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onNavigate = {},
        onRetryClick = {},
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertExists()
    onNodeWithText(getString(UiString.core_ui_retry)).assertDoesNotExist()
  }

  @Test
  fun `test blank slate is not visible when error state is null`() = uiTest {
    val uiState = HomeViewState.initial().copy(
      error = null,
      retryAction = null,
    )

    setContentWithTheme {
      HomeContent(
        viewState = uiState,
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onNavigate = {},
        onRetryClick = {},
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertDoesNotExist()
  }

  @Test
  fun `test empty filter blank slate when is empty and filter mode`() = uiTest {
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
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onNavigate = {},
        onRetryClick = {},
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    onNodeWithText(getString(Res.string.home__empty_filtered_result_title)).assertIsDisplayed()
    onNodeWithText(
      getString(Res.string.home__empty_filtered_result_description),
    ).assertIsDisplayed()
  }
}
