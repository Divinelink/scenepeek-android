package com.divinelink.core.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.model.filter.HomeFilter
import com.divinelink.core.model.filter.SelectableFilter
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.clear_filters_button_content_description
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class FilterBarTest : ComposeTest() {

  private val filters = HomeFilter.entries

  @Test
  fun clearButtonClickTest() = uiTest {
    var clearIsClicked = false
    setContent {
      FilterBar(
        filters = listOf(
          HomeFilter.Favorites(isSelected = true),
          HomeFilter.TopRated(isSelected = false),
        ),
        onFilterClick = {},
        onClearClick = {
          clearIsClicked = true
        },
      )
    }

    val clearFilterButtonContentDescription = getString(
      UiString.clear_filters_button_content_description,
    )

    onNodeWithContentDescription(clearFilterButtonContentDescription)
      .assertIsDisplayed()
      .performClick()

    clearIsClicked shouldBe true
  }

  @Test
  fun filterBarActionClickTest() = uiTest {
    var clickedFilter: SelectableFilter? = null

    setContent {
      FilterBar(
        filters = filters,
        onFilterClick = { filter ->
          clickedFilter = filter
        },
        onClearClick = {},
      )
    }

    onNodeWithText("Favorites").assertIsDisplayed().performClick()

    clickedFilter shouldBe HomeFilter.Favorites(isSelected = false)
  }

  @Test
  fun clearButtonNotDisplayedTest() = uiTest {
    setContent {
      FilterBar(
        filters = filters,
        onFilterClick = {},
        onClearClick = {},
      )
    }

    val clearFilterButtonContentDescription = getString(
      UiString.clear_filters_button_content_description,
    )

    onNodeWithContentDescription(clearFilterButtonContentDescription).assertDoesNotExist()
  }
}
