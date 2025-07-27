package com.divinelink.feature.add.to.account.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.TestAddItemToListUseCase
import com.divinelink.core.testing.usecase.TestFetchUserListsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.add.to.account.R
import com.divinelink.feature.add.to.account.list.ui.AddToListModalBottomSheet
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class AddToListModalBottomSheetTest : ComposeTest() {

  private val fetchUserListsUseCase = TestFetchUserListsUseCase()
  private val addItemToListUseCase = TestAddItemToListUseCase()
  private val savedStateHandle = SavedStateHandle(
    mapOf(
      "id" to 1234,
      "mediaType" to MediaType.MOVIE,
    ),
  )

  @Test
  fun `test AddToListModalBottomSheet`() {
    fetchUserListsUseCase.mockResponse(
      Result.success(ListItemFactory.page1()),
    )

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      AddToListModalBottomSheet(
        onDismissRequest = {},
        onNavigateToTMDBAuth = {},
        onNavigateToCreateList = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.BOTTOM_SHEET).assertIsDisplayed()
    }
  }

  @Test
  fun `test AddToListModalBottomSheet with empty data`() {
    fetchUserListsUseCase.mockResponse(
      Result.success(
        ListItemFactory.page1().copy(
          list = emptyList(),
        ),
      ),
    )

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      AddToListModalBottomSheet(
        onDismissRequest = {},
        onNavigateToTMDBAuth = {},
        onNavigateToCreateList = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Modal.BOTTOM_SHEET).assertIsDisplayed()
      onNodeWithText(getString(R.string.feature_add_to_account_empty_lists)).assertIsDisplayed()
    }
  }

  @Test
  fun `test login when unauthenticated`() {
    var navigatedToTMDBAuth = false

    val channel = Channel<Result<PaginationData<ListItem>>>()
    fetchUserListsUseCase.mockResponse(channel)

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      AddToListModalBottomSheet(
        onDismissRequest = {},
        onNavigateToTMDBAuth = {
          navigatedToTMDBAuth = true
        },
        onNavigateToCreateList = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      channel.trySend(
        Result.failure(SessionException.Unauthenticated()),
      )

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(com.divinelink.core.ui.R.string.core_ui_login))
        .assertIsDisplayed()
        .performClick()

      assertTrue { navigatedToTMDBAuth }

      channel.trySend(
        Result.success(ListItemFactory.page1()),
      )

      onNodeWithText(getString(R.string.feature_add_to_account_list_title)).assertIsDisplayed()
    }
  }

  @Test
  fun `test add to list with success`() = runTest {
    fetchUserListsUseCase.mockResponse(
      flowOf(
        Result.success(ListItemFactory.page1()),
        Result.success(ListItemFactory.page2()),
      ),
    )

    val channel: Channel<Result<Boolean>> = Channel()
    addItemToListUseCase.mockResponse(channel)

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      AddToListModalBottomSheet(
        onDismissRequest = {},
        onNavigateToTMDBAuth = {},
        onNavigateToCreateList = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.feature_add_to_account_list_title)).assertIsDisplayed()

      onNodeWithTag(TestTags.LINEAR_LOADING_INDICATOR).assertIsNotDisplayed()

      onNodeWithText("Top Movies").assertIsDisplayed().performClick()

      onNodeWithTag(TestTags.LINEAR_LOADING_INDICATOR).assertIsDisplayed()

      channel.trySend(Result.success(true))

      onNodeWithTag(TestTags.LINEAR_LOADING_INDICATOR).assertIsNotDisplayed()
    }
  }

  @Test
  fun `test onLoadMore with success`() = runTest {
    val channel = Channel<Result<PaginationData<ListItem>>>()
    fetchUserListsUseCase.mockResponse(channel)

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      savedStateHandle = savedStateHandle,
    )

    setContentWithTheme {
      AddToListModalBottomSheet(
        onDismissRequest = {},
        onNavigateToTMDBAuth = {},
        onNavigateToCreateList = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
      channel.trySend(Result.success(ListItemFactory.page1Many()))
      onNodeWithText(getString(R.string.feature_add_to_account_list_title)).assertIsDisplayed()

      onNodeWithText(ListItemFactory.page1Many().list.first().name).assertIsDisplayed()

      channel.trySend(Result.success(ListItemFactory.page2Many()))

      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).performScrollToIndex(21)

      onNodeWithText(ListItemFactory.page1Many().list.last().name).assertIsDisplayed()

      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).performScrollToIndex(30)

      onNodeWithText(ListItemFactory.page2Many().list.last().name).assertIsDisplayed()
    }
  }
}
