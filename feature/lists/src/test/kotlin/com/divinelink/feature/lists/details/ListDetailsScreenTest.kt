package com.divinelink.feature.lists.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestFetchListDetailsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.lists.details.ui.ListDetailsScreen
import com.google.common.truth.Truth.assertThat
import java.net.UnknownHostException
import kotlin.test.Test

class ListDetailsScreenTest : ComposeTest() {

  private val savedStateHandle = SavedStateHandle(
    mapOf(
      "id" to 1,
      "name" to "Test List",
    ),
  )

  @Test
  fun `test top app bar is displayed`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()
    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithText("Test List").assertIsDisplayed()
    }
  }

  @Test
  fun `test on navigate up`() {
    var navigatedUp = false
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {
          navigatedUp = true
        },
        onNavigateToMediaDetails = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
        ),
      )
    }

    with(composeTestRule) {
      assertThat(navigatedUp).isFalse()
      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick()
      assertThat(navigatedUp).isTrue()
    }
  }

  @Test
  fun `test refresh action`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.empty()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsDisplayed()
      onNodeWithTag(TestTags.Lists.Details.CONTENT).assertIsNotDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.mustWatch()),
      )

      onNodeWithTag(TestTags.Lists.Details.PULL_TO_REFRESH).performTouchInput {
        swipeDown()
      }

      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Lists.Details.CONTENT).assertIsDisplayed()

      onNodeWithText("The Wire").assertIsDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.empty()),
      )

      onNodeWithTag(TestTags.Lists.Details.PULL_TO_REFRESH).performTouchInput {
        swipeDown()
      }

      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsDisplayed()
      onNodeWithTag(TestTags.Lists.Details.CONTENT).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test on media click`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()
    var detailsRoute: DetailsRoute? = null

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.mustWatch()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {
          detailsRoute = it
        },
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Lists.Details.CONTENT).assertIsDisplayed()

      assertThat(detailsRoute).isNull()

      onNodeWithText("The Wire").assertIsDisplayed().performClick()

      assertThat(detailsRoute).isEqualTo(
        DetailsRoute(
          id = 1438,
          mediaType = MediaType.TV,
          isFavorite = null,
        ),
      )
    }
  }

  @Test
  fun `test on load more`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.success(ListDetailsFactory.page1()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Details.EMPTY_ITEM).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Lists.Details.CONTENT).assertIsDisplayed()

      onNodeWithText("Fight club 1").assertIsDisplayed()
      onNodeWithText("Fight club 16").assertIsNotDisplayed()
      onNodeWithText("Fight club 40").assertIsNotDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.page2()),
      )

      onNodeWithTag(TestTags.Lists.Details.CONTENT).performScrollToIndex(16)

      onNodeWithText("Fight club 1").assertIsNotDisplayed()
      onNodeWithText("Fight club 40").assertIsNotDisplayed()
      onNodeWithText("Fight club 16").assertIsDisplayed()

      onNodeWithTag(TestTags.Lists.Details.CONTENT).performScrollToIndex(39)
      onNodeWithText("Fight club 40").assertIsDisplayed()
    }
  }

  @Test
  fun `test on refresh with initial offline error`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.failure(UnknownHostException()),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.mustWatch()),
      )

      onNodeWithText("Retry").assertIsDisplayed().performClick()

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Lists.Details.CONTENT).assertIsDisplayed()
    }
  }

  @Test
  fun `test on refresh with generic error`() {
    val fetchListDetailsUseCase = TestFetchListDetailsUseCase()

    fetchListDetailsUseCase.mockResponse(
      Result.failure(Exception("Foo")),
    )

    setVisibilityScopeContent {
      ListDetailsScreen(
        onNavigateUp = {},
        onNavigateToMediaDetails = {},
        viewModel = ListDetailsViewModel(
          fetchListDetailsUseCase = fetchListDetailsUseCase.mock,
          savedStateHandle = savedStateHandle,
        ),
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()

      fetchListDetailsUseCase.mockResponse(
        Result.success(ListDetailsFactory.mustWatch()),
      )

      onNodeWithText("Retry").assertIsDisplayed().performClick()

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsNotDisplayed()
      onNodeWithTag(TestTags.Lists.Details.CONTENT).assertIsDisplayed()
    }
  }
}
