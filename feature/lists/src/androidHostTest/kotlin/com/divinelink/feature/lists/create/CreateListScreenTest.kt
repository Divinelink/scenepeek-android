package com.divinelink.feature.lists.create

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.network.list.model.update.UpdateListResponse
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.hasClickLabel
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.TestCreateListUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_delete
import com.divinelink.core.ui.resources.core_ui_select_media_backdrop_image
import com.divinelink.feature.lists.create.backdrop.SelectBackdropViewModel
import com.divinelink.feature.lists.create.ui.CreateListScreen
import com.divinelink.feature.lists.resources.feature_lists_delete_list
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import org.jetbrains.compose.resources.getString
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.feature.lists.resources.Res as R

class CreateListScreenTest : ComposeTest() {

  private val createListUseCase = TestCreateListUseCase()
  private val repository = TestListRepository()

  @BeforeTest
  fun setup() {
    startKoin {
      // Do nothing
    }
  }

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun `test on delete click open delete dialog`() = uiTest {
    var navigatedBackToLists = false
    setVisibilityScopeContent {
      CreateListScreen(
        viewModel = CreateListViewModel(
          createListUseCase = createListUseCase.mock,
          repository = repository.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 0,
              "name" to "List name",
              "backdropPath" to "",
              "description" to "Description of the list",
              "public" to true,
            ),
          ),
        ),
        onNavigateBackToLists = {
          navigatedBackToLists = true
        },
        onNavigateUp = {},
      )
    }

    onNodeWithTag(TestTags.Dialogs.DELETE_REQUEST).assertIsNotDisplayed()

    onNodeWithTag(TestTags.Components.SCROLLABLE_CONTENT).performScrollToNode(
      hasText(getString(R.string.feature_lists_delete_list)),
    )

    onNodeWithText(getString(R.string.feature_lists_delete_list)).performClick()
    onNodeWithTag(TestTags.Dialogs.DELETE_REQUEST).assertIsDisplayed()

    repository.mockDeleteListResponse(Result.success(Unit))

    onNodeWithText(getString(UiString.core_ui_delete)).performClick()

    navigatedBackToLists shouldBe true
  }

  @Test
  fun `test select backdrop and update list`() = uiTest {
    var navigatedUp = false
    repository.mockFetchListsBackdrops(
      flowOf(
        mapOf(
          "Fight club" to "/path/to/fight_club.jpg",
          "Inception" to "/path/to/inception.jpg",
        ),
      ),
    )

    declare {
      SelectBackdropViewModel(
        id = 1234,
        repository = repository.mock,
      )
    }

    repository.mockEditListResponse(
      Result.success(
        UpdateListResponse(
          success = true,
          statusCode = 1,
        ),
      ),
    )

    setVisibilityScopeContent {
      CreateListScreen(
        viewModel = CreateListViewModel(
          createListUseCase = createListUseCase.mock,
          repository = repository.mock,
          savedStateHandle = SavedStateHandle(
            mapOf(
              "id" to 1234,
              "name" to "List name",
              "backdropPath" to "/path/to/backdrop.jpg",
              "description" to "Description of the list",
              "public" to true,
            ),
          ),
        ),
        onNavigateBackToLists = {},
        onNavigateUp = {
          navigatedUp = true
        },
      )
    }

    onNode(
      hasClickLabel(getString(UiString.core_ui_select_media_backdrop_image)),
    ).performClick()

    onNodeWithTag(TestTags.Modal.BOTTOM_SHEET).assertIsDisplayed()

    onNodeWithText("Fight club").assertIsDisplayed()
    onNodeWithText("Inception").assertIsDisplayed().performClick()

    onNodeWithTag(TestTags.Modal.BOTTOM_SHEET).assertIsNotDisplayed()

    onNodeWithText("Save").performClick()

    navigatedUp shouldBe true
  }
}
