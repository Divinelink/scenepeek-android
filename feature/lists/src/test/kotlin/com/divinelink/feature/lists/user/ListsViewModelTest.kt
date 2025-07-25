package com.divinelink.feature.lists.user

import com.divinelink.core.fixtures.model.list.ListItemFactory
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.SessionException
import com.divinelink.core.model.list.ListData
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.feature.lists.R
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.net.UnknownHostException
import kotlin.test.Test

class ListsViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val robot = ListsViewModelTestRobot()

  @Test
  fun `test fetch lists when unauthenticated`() = runTest {
    robot
      .mockFetchUserData(Result.failure(SessionException.Unauthenticated()))
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          error = BlankSlateState.Unauthenticated(
            UIText.ResourceText(R.string.feature_lists_login_description),
          ),
          page = 1,
          lists = ListData.Initial,
          isLoading = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test fetch lists with failure`() = runTest {
    robot
      .mockFetchUserData(Result.failure(Exception("Foo")))
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          error = BlankSlateState.Generic,
          page = 1,
          lists = ListData.Initial,
          isLoading = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test fetch lists when offline`() = runTest {
    robot
      .mockFetchUserData(Result.failure(UnknownHostException()))
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          error = BlankSlateState.Offline,
          page = 1,
          lists = ListData.Initial,
          isLoading = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test fetch lists when authenticated`() = runTest {
    robot
      .mockFetchUserData(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test loadMore lists when page is less than total pages`() = runTest {
    robot
      .mockFetchUserData(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserData(response = Result.success(ListItemFactory.page2()))
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = true,
          ),
          ListsUiState.initial.copy(
            page = 3,
            lists = ListData.Data(
              PaginationData(
                page = 2,
                totalPages = 2,
                totalResults = 6,
                list = ListItemFactory.page1().list + ListItemFactory.page2().list,
              ),
            ),
            isLoading = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test loadMore lists with generic error does not remove pages`() = runTest {
    robot
      .mockFetchUserData(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserData(response = Result.failure(Exception("Foo")))
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = true,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test loadMore lists with unauthenticated does not clears lists`() = runTest {
    robot
      .mockFetchUserData(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserData(response = Result.failure(SessionException.Unauthenticated()))
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = true,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test loadMore lists with generic error does not clears lists`() = runTest {
    robot
      .mockFetchUserData(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserData(response = Result.failure(Exception("Foo")))
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = true,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test refresh resets pages`() = runTest {
    robot
      .mockFetchUserData(
        Result.success(ListItemFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListsUiState.initial.copy(
          page = 2,
          lists = ListData.Data(ListItemFactory.page1()),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserData(response = Result.success(ListItemFactory.page2()))
      .onLoadMore()
      .assertUiState(
        ListsUiState.initial.copy(
          page = 3,
          lists = ListData.Data(
            PaginationData(
              page = 2,
              totalPages = 2,
              totalResults = 6,
              list = ListItemFactory.page1().list + ListItemFactory.page2().list,
            ),
          ),
          isLoading = false,
          loadingMore = false,
        ),
      )
      .mockFetchUserData(
        Result.success(ListItemFactory.page1()),
      )
      .expectUiStates(
        action = {
          onRefresh()
        },
        uiStates = listOf(
          ListsUiState.initial.copy(
            page = 3,
            lists = ListData.Data(
              PaginationData(
                page = 2,
                totalPages = 2,
                totalResults = 6,
                list = ListItemFactory.page1().list + ListItemFactory.page2().list,
              ),
            ),
            isLoading = false,
            loadingMore = false,
            refreshing = false,
          ),
          ListsUiState.initial.copy(
            page = 3,
            lists = ListData.Data(
              PaginationData(
                page = 2,
                totalPages = 2,
                totalResults = 6,
                list = ListItemFactory.page1().list + ListItemFactory.page2().list,
              ),
            ),
            isLoading = false,
            loadingMore = false,
            refreshing = true,
          ),
          ListsUiState.initial.copy(
            page = 2,
            lists = ListData.Data(ListItemFactory.page1()),
            isLoading = false,
            loadingMore = false,
            refreshing = false,
          ),
        ),
      )
  }
}
