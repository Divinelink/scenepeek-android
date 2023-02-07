package com.andreolas.movierama.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.andreolas.movierama.ui.components.SEARCH_BAR_LOADING_INDICATOR_TAG
import com.andreolas.movierama.ui.components.SearchBar
import com.andreolas.movierama.ui.components.ToolbarState
import org.junit.Rule
import org.junit.Test

class SearchBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun searchBarLoadingTest() {
        composeTestRule.setContent {
            SearchBar(
                searchValue = null,
                onClearClicked = {},
                onSearchFieldChanged = {},
                actions = {},
                state = ToolbarState.Focused,
                isLoading = true,
            )
        }

        composeTestRule
            .onNodeWithTag(SEARCH_BAR_LOADING_INDICATOR_TAG)
            .assertIsDisplayed()
    }
}
