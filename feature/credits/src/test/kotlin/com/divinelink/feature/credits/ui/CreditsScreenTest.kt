package com.divinelink.feature.credits.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.navigator.FakeDestinationsNavigator
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.TestFetchCreditsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.credits.R
import kotlinx.coroutines.flow.flowOf
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class CreditsScreenTest : ComposeTest() {

  private lateinit var navigator: FakeDestinationsNavigator
  private lateinit var fetchCreditsUseCase: TestFetchCreditsUseCase
  private lateinit var savedStateHandle: SavedStateHandle

  @BeforeTest
  fun setUp() {
    navigator = FakeDestinationsNavigator()
    fetchCreditsUseCase = TestFetchCreditsUseCase()
    savedStateHandle = SavedStateHandle(
      mapOf(
        "id" to 2316L,
        "mediaType" to MediaType.TV,
      ),
    )
  }

  @Test
  fun `test topAppBar is visible`() {
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      CreditsScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithText(getString(R.string.feature_credits_cast_and_crew_title)).assertIsDisplayed()
    }
  }

  @Test
  fun `test back button is visible`() {
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      CreditsScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithContentDescription(
        getString(uiR.string.core_ui_navigate_up_button_content_description),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test cast content is visible`() {
    fetchCreditsUseCase.mockSuccess(
      flowOf(Result.success(AggregatedCreditsFactory.credits())),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      CreditsScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.Credits.CAST_CREDITS_CONTENT).assertIsDisplayed()
      onNodeWithText(
        AggregatedCreditsFactory.credits().cast.first().name,
      ).assertIsDisplayed()

      onNodeWithTag(TestTags.Credits.CAST_CREDITS_CONTENT).performScrollToNode(
        matcher = hasText(AggregatedCreditsFactory.credits().cast.last().name),
      )

      onNodeWithText(
        AggregatedCreditsFactory.credits().cast.last().name,
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test crew content is visible`() {
    fetchCreditsUseCase.mockSuccess(
      flowOf(Result.success(AggregatedCreditsFactory.credits())),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      CreditsScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      // Scroll pager to crew tab
      // Tab has a text of Crew + number of unique crew members
      onNodeWithText("Crew (12)").performClick()

      onNodeWithTag(TestTags.Credits.CREW_CREDITS_CONTENT).assertIsDisplayed()
      onNodeWithText(
        AggregatedCreditsFactory.credits().crewDepartments.first().department,
      ).assertIsDisplayed()

      onNodeWithTag(TestTags.Credits.CREW_CREDITS_CONTENT).performScrollToNode(
        matcher = hasText(AggregatedCreditsFactory.credits().crewDepartments.last().department),
      )

      onNodeWithText(
        AggregatedCreditsFactory.credits().crewDepartments.last().department,
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test cast empty content`() {
    fetchCreditsUseCase.mockSuccess(
      flowOf(Result.success(AggregatedCreditsFactory.credits().copy(cast = emptyList()))),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      CreditsScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(R.string.feature_credits_cast_missing)).assertIsDisplayed()
    }
  }

  @Test
  fun `test crew empty content`() {
    fetchCreditsUseCase.mockSuccess(
      flowOf(
        Result.success(AggregatedCreditsFactory.credits().copy(crewDepartments = emptyList())),
      ),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      CreditsScreen(
        navigator = navigator,
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      // Scroll pager to crew tab
      // Tab has a text of Crew + number of unique crew members
      onNodeWithText("Crew (0)").performClick()

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(R.string.feature_credits_crew_missing)).assertIsDisplayed()
    }
  }
}
