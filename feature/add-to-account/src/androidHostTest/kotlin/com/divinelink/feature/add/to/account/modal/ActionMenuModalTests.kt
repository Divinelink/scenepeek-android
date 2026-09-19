package com.divinelink.feature.add.to.account.modal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.repository.TestDetailsRepository
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.repository.TestTrackRepository
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_select
import com.divinelink.core.ui.resources.core_ui_share
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class ActionMenuModalTests : ComposeTest() {

  private val repository = TestListRepository()
  private val detailsRepository = TestDetailsRepository()
  private val trackRepository = TestTrackRepository()
  private val markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()

  @Test
  fun `test ActionMenuModal for ListDetails`() = uiTest {
    val viewModel = ActionMenuViewModel(
      listRepository = repository.mock,
      trackRepository = trackRepository.mock,
      detailsRepository = detailsRepository.mock,
      entryPoint = ActionMenuEntryPoint.ListDetails(
        media = MediaItemFactory.theWire(),
        listId = 1234,
        listName = "TV Shows",
      ),
      markAsFavoriteUseCase = markAsFavoriteUseCase,
    )
    var mediaItem: MediaItem? = null

    setContentWithTheme {
      ActionMenuModal(
        entryPoint = ActionMenuEntryPoint.ListDetails(
          media = MediaItemFactory.theWire(),
          listId = 1234,
          listName = "TV Shows",
        ),
        onDismissRequest = {},
        onMultiSelect = {
          mediaItem = it
        },
        viewModel = viewModel,
        onNavigate = {},
      )
    }

    onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

    onNodeWithTag(TestTags.Components.SCROLLABLE_CONTENT).performTouchInput {
      swipeUp()
    }

    onNodeWithText(getString(UiString.core_ui_share)).assertIsDisplayed()

    mediaItem shouldBe null

    onNodeWithText(getString(UiString.core_ui_select)).assertIsDisplayed().performClick()

    mediaItem shouldBe MediaItemFactory.theWire()
  }

  @Test
  fun `test save action on list details`() = uiTest {
    val viewModel = ActionMenuViewModel(
      listRepository = repository.mock,
      trackRepository = trackRepository.mock,
      detailsRepository = detailsRepository.mock,
      entryPoint = ActionMenuEntryPoint.ListDetails(
        media = MediaItemFactory.theWire(),
        listId = 1234,
        listName = "TV Shows",
      ),
      markAsFavoriteUseCase = markAsFavoriteUseCase,
    )

    markAsFavoriteUseCase.mockMarkAsFavoriteResult(
      media = MediaItemFactory.theWire(),
      result = Result.success(true),
    )

    setContentWithTheme {
      ActionMenuModal(
        entryPoint = ActionMenuEntryPoint.ListDetails(
          media = MediaItemFactory.theWire(),
          listId = 1234,
          listName = "TV Shows",
        ),
        onDismissRequest = {},
        onMultiSelect = {},
        viewModel = viewModel,
        onNavigate = {},
      )
    }

    onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

    onNodeWithTag(TestTags.Components.Button.SAVE_BUTTON.format("false"))
      .assertIsDisplayed()
      .performClick()

    onNodeWithTag(TestTags.Components.Button.SAVE_BUTTON.format("true"))
      .assertIsDisplayed()
  }

  @Test
  fun `test ActionMenuModal for Other`() = uiTest {
    val viewModel = ActionMenuViewModel(
      listRepository = repository.mock,
      trackRepository = trackRepository.mock,
      detailsRepository = detailsRepository.mock,
      entryPoint = ActionMenuEntryPoint.Other(
        media = MediaItemFactory.theWire(),
      ),
      markAsFavoriteUseCase = markAsFavoriteUseCase,
    )
    var mediaItem: MediaItem? = null

    setContentWithTheme {
      ActionMenuModal(
        entryPoint = ActionMenuEntryPoint.Other(
          media = MediaItemFactory.theWire(),
        ),
        onDismissRequest = {},
        onMultiSelect = {
          mediaItem = it
        },
        viewModel = viewModel,
        onNavigate = {},
      )
    }

    onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

    onNodeWithText(getString(UiString.core_ui_share)).assertIsDisplayed()

    mediaItem shouldBe null

    onNodeWithText(getString(UiString.core_ui_select)).assertIsNotDisplayed()
  }
}
