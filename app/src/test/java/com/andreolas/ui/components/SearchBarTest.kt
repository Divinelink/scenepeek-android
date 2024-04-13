package com.andreolas.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.andreolas.movierama.ui.components.MovieRamaSearchBar
import com.andreolas.movierama.ui.components.SEARCH_BAR_LOADING_INDICATOR_TAG
import com.andreolas.movierama.ui.components.ToolbarState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchBarTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @OptIn(ExperimentalMaterial3Api::class)
  @Test
  fun searchBarLoadingTest() {
    composeTestRule.setContent {
      MovieRamaSearchBar(
        query = null,
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
