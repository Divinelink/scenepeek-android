package com.divinelink.feature.credits.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.TestFetchCreditsUseCase
import com.divinelink.core.testing.usecase.TestSpoilersObfuscationUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_hidden_spoilers_button
import com.divinelink.core.ui.core_ui_hide_spoilers_tooltip
import com.divinelink.core.ui.core_ui_navigate_up_button_content_description
import com.divinelink.core.ui.core_ui_show_spoilers_tooltip
import com.divinelink.core.ui.core_ui_visible_spoilers_button
import com.divinelink.feature.credits.Res
import com.divinelink.feature.credits.feature_credits_cast_and_crew_title
import com.divinelink.feature.credits.feature_credits_cast_missing
import com.divinelink.feature.credits.feature_credits_crew_missing
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.getString
import kotlin.test.BeforeTest
import kotlin.test.Test

class CreditsScreenTest : ComposeTest() {

  private lateinit var fetchCreditsUseCase: TestFetchCreditsUseCase
  private lateinit var spoilersObfuscationUseCase: SpoilersObfuscationUseCase
  private lateinit var savedStateHandle: SavedStateHandle

  @BeforeTest
  override fun setUp() {
    fetchCreditsUseCase = TestFetchCreditsUseCase()
    spoilersObfuscationUseCase = TestSpoilersObfuscationUseCase().useCase(false)
    savedStateHandle = SavedStateHandle(
      mapOf(
        "id" to 2316L,
        "mediaType" to MediaType.TV.value,
      ),
    )
  }

  @Test
  fun `test topAppBar is visible`() = uiTest {
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      CreditsScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    onNodeWithText(getString(Res.string.feature_credits_cast_and_crew_title)).assertIsDisplayed()

  }

  @Test
  fun `test back button is visible`() = uiTest {
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      CreditsScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    onNodeWithContentDescription(
      getString(UiString.core_ui_navigate_up_button_content_description),
    ).assertIsDisplayed()

  }

  @Test
  fun `test cast content is visible`() = uiTest {
    fetchCreditsUseCase.mockSuccess(
      flowOf(Result.success(AggregatedCreditsFactory.credits())),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      CreditsScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

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

  @Test
  fun `test crew content is visible`() = uiTest {
    fetchCreditsUseCase.mockSuccess(
      flowOf(Result.success(AggregatedCreditsFactory.credits())),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      CreditsScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

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

  @Test
  fun `test cast empty content`() = uiTest {
    fetchCreditsUseCase.mockSuccess(
      flowOf(Result.success(AggregatedCreditsFactory.credits().copy(cast = emptyList()))),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      CreditsScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    onNodeWithText(getString(Res.string.feature_credits_cast_missing)).assertIsDisplayed()

  }

  @Test
  fun `test crew empty content`() = uiTest {
    fetchCreditsUseCase.mockSuccess(
      flowOf(
        Result.success(AggregatedCreditsFactory.credits().copy(crewDepartments = emptyList())),
      ),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      CreditsScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    // Scroll pager to crew tab
    // Tab has a text of Crew + number of unique crew members
    onNodeWithText("Crew (0)").performClick()

    onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
    onNodeWithText(getString(Res.string.feature_credits_crew_missing)).assertIsDisplayed()

  }

  @Test
  fun `test obfuscate spoilers button`() = uiTest {
    fetchCreditsUseCase.mockSuccess(
      flowOf(Result.success(AggregatedCreditsFactory.credits())),
    )
    val viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      CreditsScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    onNodeWithContentDescription(getString(UiString.core_ui_visible_spoilers_button))
      .assertIsDisplayed()

    onNodeWithTag(TestTags.Menu.SPOILERS_OBFUSCATION)
      .assertIsDisplayed()
      .performTouchInput {
        longClick()
      }

    onNodeWithText(getString(UiString.core_ui_hide_spoilers_tooltip)).assertIsDisplayed()

    onNodeWithTag(TestTags.Menu.SPOILERS_OBFUSCATION)
      .assertIsDisplayed()
      .performClick()

    onNodeWithContentDescription(getString(UiString.core_ui_hidden_spoilers_button))
      .assertIsDisplayed()

    onNodeWithTag(TestTags.Menu.SPOILERS_OBFUSCATION)
      .assertIsDisplayed()
      .performTouchInput {
        longClick()
      }

    onNodeWithText(getString(UiString.core_ui_show_spoilers_tooltip)).assertIsDisplayed()

  }
}
