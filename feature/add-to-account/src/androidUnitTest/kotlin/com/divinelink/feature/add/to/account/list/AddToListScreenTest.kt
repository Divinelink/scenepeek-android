package com.divinelink.feature.add.to.account.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.usecase.TestAddItemToListUseCase
import com.divinelink.core.testing.usecase.TestFetchUserListsUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.add.to.account.R
import com.divinelink.feature.add.to.account.list.ui.AddToListScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class AddToListScreenTest : ComposeTest() {

  private val fetchUserListsUseCase = TestFetchUserListsUseCase()
  private val addItemToListUseCase = TestAddItemToListUseCase()
  private val listRepository = TestListRepository()
  private val savedStateHandle = SavedStateHandle(
    mapOf(
      "id" to 1234,
      "mediaType" to MediaType.MOVIE,
    ),
  )

  @Test
  fun `test AddToListModalBottomSheet`() = runTest {
    listRepository.mockGetItemStatus(result = Result.success(false))

    fetchUserListsUseCase.mockResponse(
      Result.success(ListItemFactory.page1()),
    )

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      repository = listRepository.mock,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      AddToListScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Add.SCREEN).assertIsDisplayed()
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
      repository = listRepository.mock,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      AddToListScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Lists.Add.SCREEN).assertIsDisplayed()
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
      repository = listRepository.mock,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      AddToListScreen(
        onNavigate = {
          if (it is Navigation.TMDBAuthRoute) {
            navigatedToTMDBAuth = true
          }
        },
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      channel.trySend(
        Result.failure(SessionException.Unauthenticated()),
      )

      onNodeWithTag(TestTags.BLANK_SLATE).assertIsDisplayed()
      onNodeWithText(getString(com.divinelink.core.ui.UiString.core_ui_login))
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
    listRepository.mockGetItemStatus(result = Result.success(false))

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
      repository = listRepository.mock,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      AddToListScreen(
        onNavigate = {},
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
    listRepository.mockGetItemStatus(result = Result.success(false))

    val channel = Channel<Result<PaginationData<ListItem>>>()
    fetchUserListsUseCase.mockResponse(channel)

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      repository = listRepository.mock,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      AddToListScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
      channel.trySend(Result.success(ListItemFactory.page1Many()))
      onNodeWithText(getString(R.string.feature_add_to_account_list_title)).assertIsDisplayed()

      onNodeWithText(ListItemFactory.page1Many().list.first().name).assertIsDisplayed()

      channel.trySend(Result.success(ListItemFactory.page2Many()))

      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).performScrollToIndex(19)

      onNodeWithText(ListItemFactory.page1Many().list.last().name).assertIsDisplayed()

      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).performScrollToIndex(29)

      onNodeWithText(ListItemFactory.page2Many().list.last().name).assertIsDisplayed()
    }
  }

  @Test
  fun `test get item status`() = runTest {
    val mediaReference = MediaReference(
      mediaId = 1234,
      mediaType = MediaType.MOVIE,
    )

    val channel = Channel<Result<PaginationData<ListItem>>>()
    fetchUserListsUseCase.mockResponse(channel)

    ListItemFactory.page1Many().list.forEachIndexed { index, list ->
      listRepository.mockGetItemStatus(
        item = mediaReference,
        listId = list.id,
        // Mark as added lists with even index (0, 2, 4 etc.)
        result = Result.success(index % 2 == 0),
      )
    }

    ListItemFactory.page2Many().list.forEach { list ->
      listRepository.mockGetItemStatus(
        item = mediaReference,
        listId = list.id,
        result = Result.failure(AppException.Unknown()),
      )
    }

    val viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      repository = listRepository.mock,
      savedStateHandle = savedStateHandle,
    )

    setVisibilityScopeContent {
      AddToListScreen(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.LOADING_CONTENT).assertIsDisplayed()
      channel.trySend(Result.success(ListItemFactory.page1Many()))
      onNodeWithText(getString(R.string.feature_add_to_account_list_title)).assertIsDisplayed()

      channel.trySend(Result.success(ListItemFactory.page2Many()))
      ListItemFactory.page1Many().list.forEachIndexed { index, list ->
        if (index % 2 == 0) {
          onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).performScrollToNode(
            hasContentDescription("Added on list ${list.name}"),
          )
        }
      }

      channel.trySend(Result.success(ListItemFactory.page2Many()))

      ListItemFactory.page2Many().list.forEach { list ->
        onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).performScrollToNode(
          hasText(list.name),
        )
        onNodeWithContentDescription("Added on list ${list.name}").assertIsNotDisplayed()
      }

      // Scroll back to first item and assert that the get item status call is only fetched once
      // per item.
      onNodeWithTag(TestTags.Lists.SCROLLABLE_CONTENT).performScrollToIndex(0)

      ListItemFactory.page1Many().list.forEach { list ->
        listRepository.verifyGetItemStatus(
          listId = list.id,
          item = mediaReference,
          times = 1,
        )
      }
    }
  }
}
