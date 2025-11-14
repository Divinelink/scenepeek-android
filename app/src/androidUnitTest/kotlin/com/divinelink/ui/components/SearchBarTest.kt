package com.divinelink.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.ScenePeekSearchBar
import com.divinelink.core.ui.components.ToolbarState
import kotlin.test.Test

@OptIn(ExperimentalMaterial3Api::class)
class SearchBarTest : ComposeTest() {

  @Test
  fun `test loading is visible when focused`() {
    composeTestRule.setContent {
      ScenePeekSearchBar(
        query = "search",
        onClearClicked = {},
        onSearchFieldChanged = {},
        actions = {},
        state = ToolbarState.Focused,
        isLoading = true,
        focusTrigger = 0,
        isSearchable = true,
        onFocused = {},
      )
    }

    composeTestRule
      .onNodeWithContentDescription(getString(UiString.core_ui_toolbar_search_placeholder))
      .performClick()

    composeTestRule
      .onNodeWithTag(TestTags.Components.SearchBar.LOADING_INDICATOR)
      .assertIsDisplayed()
  }

  @Test
  fun `test loading is not visible without focus`() {
    composeTestRule.setContent {
      ScenePeekSearchBar(
        query = "search",
        onClearClicked = {},
        onSearchFieldChanged = {},
        actions = {},
        state = ToolbarState.Unfocused,
        isLoading = true,
        focusTrigger = 0,
        isSearchable = true,
        onFocused = {},
      )
    }

    composeTestRule
      .onNodeWithTag(TestTags.Components.SearchBar.LOADING_INDICATOR)
      .assertDoesNotExist()
  }
}
