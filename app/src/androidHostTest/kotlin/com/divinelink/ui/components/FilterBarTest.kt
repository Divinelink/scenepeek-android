package com.divinelink.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.Filter
import com.divinelink.core.ui.components.FilterBar
import com.divinelink.core.ui.resources.clear_filters_button_content_description
import com.google.common.truth.Truth.assertThat
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class FilterBarTest : ComposeTest() {

  private val filters = listOf(
    Filter("Action", isSelected = true),
    Filter("Comedy", isSelected = false),
    Filter("Drama", isSelected = false),
    Filter("Horror", isSelected = false),
    Filter("Romance", isSelected = false),
    Filter("Thriller", isSelected = false),
  )

  @Test
  fun clearButtonClickTest() = uiTest {
    var clearIsClicked = false
    setContent {
      FilterBar(
        filters = filters,
        onFilterClick = {},
        onClearClick = {
          clearIsClicked = true
        },
      )
    }

    val clearFilterButtonContentDescription = getString(
      UiString.clear_filters_button_content_description,
    )

    onNodeWithContentDescription(clearFilterButtonContentDescription).assertIsDisplayed()
      .performClick()

    assertThat(clearIsClicked).isTrue()
  }

  @Test
  fun filterBarActionClickTest() = uiTest {
    var actionIsClicked = false
    setContent {
      FilterBar(
        filters = filters,
        onFilterClick = { filter ->
          actionIsClicked = filter.name == "Action"
        },
        onClearClick = {},
      )
    }

    onNodeWithText("Action").assertIsDisplayed().performClick()

    assertThat(actionIsClicked).isTrue()
  }

  @Test
  fun clearButtonNotDisplayedTest() = uiTest {
    val filters = listOf(
      Filter("Romance", isSelected = false),
      Filter("Thriller", isSelected = false),
    )

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
