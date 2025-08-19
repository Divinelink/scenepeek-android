package com.divinelink.feature.add.to.account.modal

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.ui.TestTags
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class ActionMenuModalTests : ComposeTest() {

  private val repository = TestListRepository()
  private val markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()

  @Test
  fun `test ActionMenuModal for ListDetails`() {
    val viewModel = ActionMenuViewModel(
      listRepository = repository.mock,
      entryPoint = ActionMenuEntryPoint.ListDetails(
        listId = 1234,
        listName = "TV Shows",
      ),
      mediaItem = MediaItemFactory.theWire(),
      markAsFavoriteUseCase = markAsFavoriteUseCase,
    )
    var mediaItem: MediaItem? = null

    setContentWithTheme {
      ActionMenuModal(
        mediaItem = MediaItemFactory.theWire(),
        entryPoint = ActionMenuEntryPoint.ListDetails(
          listId = 1234,
          listName = "TV Shows",
        ),
        onDismissRequest = {},
        onMultiSelect = {
          mediaItem = it
        },
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

      onNodeWithText(getString(uiR.string.core_ui_share)).assertIsDisplayed()

      mediaItem shouldBe null

      onNodeWithText(getString(uiR.string.core_ui_select)).assertIsDisplayed().performClick()

      mediaItem shouldBe MediaItemFactory.theWire()
    }
  }

  @Test
  fun `test ActionMenuModal for Other`() {
    val viewModel = ActionMenuViewModel(
      listRepository = repository.mock,
      entryPoint = ActionMenuEntryPoint.Other,
      mediaItem = MediaItemFactory.theWire(),
      markAsFavoriteUseCase = markAsFavoriteUseCase,
    )
    var mediaItem: MediaItem? = null

    setContentWithTheme {
      ActionMenuModal(
        mediaItem = MediaItemFactory.theWire(),
        entryPoint = ActionMenuEntryPoint.Other,
        onDismissRequest = {},
        onMultiSelect = {
          mediaItem = it
        },
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.ACTION_MENU).assertIsDisplayed()

      onNodeWithText(getString(uiR.string.core_ui_share)).assertIsDisplayed()

      mediaItem shouldBe null

      onNodeWithText(getString(uiR.string.core_ui_select)).assertIsNotDisplayed()
    }
  }
}
