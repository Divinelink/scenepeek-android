package com.andreolas.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MovieRamaSearchBar
import com.divinelink.core.ui.components.ToolbarState
import kotlin.test.Test

@OptIn(ExperimentalMaterial3Api::class)
class SearchBarTest : ComposeTest() {

  @Test
  fun `test loading is visible when focused`() {
    composeTestRule.setContent {
      MovieRamaSearchBar(
        query = "search",
        onClearClicked = {},
        onSearchFieldChanged = {},
        actions = {},
        state = ToolbarState.Focused,
        isLoading = true,
      )
    }

    composeTestRule
      .onNodeWithContentDescription(getString(R.string.core_ui_toolbar_search_placeholder))
      .performClick()

    composeTestRule
      .onNodeWithTag(TestTags.Components.SearchBar.LOADING_INDICATOR)
      .assertIsDisplayed()
  }

  @Test
  fun `test loading is not visible without focus`() {
    composeTestRule.setContent {
      MovieRamaSearchBar(
        query = "search",
        onClearClicked = {},
        onSearchFieldChanged = {},
        actions = {},
        state = ToolbarState.Unfocused,
        isLoading = true,
      )
    }

    composeTestRule
      .onNodeWithTag(TestTags.Components.SearchBar.LOADING_INDICATOR)
      .assertDoesNotExist()
  }
}
