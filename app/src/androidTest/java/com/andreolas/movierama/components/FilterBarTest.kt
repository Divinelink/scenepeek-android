package com.andreolas.movierama.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.components.Filter
import com.andreolas.movierama.ui.components.FilterBar
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class FilterBarTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val filters = listOf(
        Filter("Action", isSelected = true),
        Filter("Comedy", isSelected = false),
        Filter("Drama", isSelected = false),
        Filter("Horror", isSelected = false),
        Filter("Romance", isSelected = false),
        Filter("Thriller", isSelected = false),
    )

    @Test
    fun clearButtonClickTest() {
        var clearIsClicked = false
        composeTestRule.setContent {
            FilterBar(
                filters = filters,
                onFilterClick = {},
                onClearClick = {
                    clearIsClicked = true
                },
            )
        }

        val clearFilterButtonContentDescription =
            composeTestRule.activity.getString(
                R.string.clear_filters_button_content_description
            )

        composeTestRule
            .onNodeWithContentDescription(clearFilterButtonContentDescription)
            .assertIsDisplayed()
            .performClick()

        assertThat(clearIsClicked).isTrue()
    }

    @Test
    fun filterBarActionClickTest() {
        var actionIsClicked = false
        composeTestRule.setContent {
            FilterBar(
                filters = filters,
                onFilterClick = { filter ->
                    actionIsClicked = filter.name == "Action"
                },
                onClearClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Action")
            .assertIsDisplayed()
            .performClick()

        assertThat(actionIsClicked).isTrue()
    }

    @Test
    fun clearButtonNotDisplayedTest() {
        val filters = listOf(
            Filter("Romance", isSelected = false),
            Filter("Thriller", isSelected = false),
        )

        composeTestRule.setContent {
            FilterBar(
                filters = filters,
                onFilterClick = {},
                onClearClick = {},
            )
        }

        val clearFilterButtonContentDescription =
            composeTestRule.activity.getString(
                R.string.clear_filters_button_content_description
            )

        composeTestRule
            .onNodeWithContentDescription(clearFilterButtonContentDescription)
            .assertDoesNotExist()
    }
}
