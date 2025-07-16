package com.divinelink.feature.lists.details

import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.navigation.route.ListDetailsRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.ui.blankslate.BlankSlateState
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.net.UnknownHostException
import kotlin.test.Test

class ListDetailsViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val robot = ListDetailsViewModelTestRobot()

  private val listDetailsRoute = ListDetailsRoute(
    id = 1,
    name = "Test List",
    backdropPath = ListDetailsFactory.mustWatch().backdropPath,
    description = ListDetailsFactory.mustWatch().description,
    public = ListDetailsFactory.mustWatch().public,
  )

  @Test
  fun `test fetch data`() {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.success(ListDetailsFactory.mustWatch()),
      )
      .buildViewModel()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test refresh correctly clears pages`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.success(ListDetailsFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.page1(),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
        ),
      )
      .mockListDetails(
        Result.success(ListDetailsFactory.page2()),
      )
      .onLoadMore()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.page1().copy(
              page = 2,
              media = ListDetailsFactory.page1().media + ListDetailsFactory.page2().media,
            ),
          ),
          page = 3,
          error = null,
          refreshing = false,
          loadingMore = false,
        ),
      )
      .mockListDetails(
        Result.success(ListDetailsFactory.mustWatch()),
      )
      .expectUiStates(
        action = {
          onRefresh()
        },
        uiStates = listOf(
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page1().copy(
                page = 2,
                media = ListDetailsFactory.page1().media + ListDetailsFactory.page2().media,
              ),
            ),
            page = 3,
            error = null,
            refreshing = false,
            loadingMore = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page1().copy(
                page = 2,
                media = ListDetailsFactory.page1().media + ListDetailsFactory.page2().media,
              ),
            ),
            page = 3,
            error = null,
            refreshing = true,
            loadingMore = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.mustWatch(),
            ),
            page = 2,
            error = null,
            refreshing = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test load more when cannot load more`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.success(ListDetailsFactory.mustWatch()),
      )
      .buildViewModel()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
        ),
      )
      .onLoadMore()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test load more correctly sets loading more`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.success(ListDetailsFactory.page1()),
      )
      .buildViewModel()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.page1(),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
        ),
      )
      .mockListDetails(
        Result.success(ListDetailsFactory.page2()),
      )
      .expectUiStates(
        action = {
          onLoadMore()
        },
        uiStates = listOf(
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page1(),
            ),
            page = 2,
            error = null,
            refreshing = false,
            loadingMore = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page1(),
            ),
            page = 2,
            error = null,
            refreshing = false,
            loadingMore = true,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page1().copy(
                page = 2,
                media = ListDetailsFactory.page1().media + ListDetailsFactory.page2().media,
              ),
            ),
            page = 3,
            error = null,
            refreshing = false,
            loadingMore = false,
          ),
        ),
      )
  }

  @Test
  fun `test load more with error when data not initial does not set error`() {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.success(ListDetailsFactory.mustWatch()),
      )
      .buildViewModel()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
        ),
      )
      .mockListDetails(
        Result.failure(Exception("Foo")),
      )
      .onLoadMore()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
          ),
          page = 2,
          error = null, // Error should not be set when data is not initial
          refreshing = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test initial with error`() {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.failure(Exception("Foo")),
      )
      .buildViewModel()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Initial(
            name = "Test List",
            backdropPath = ListDetailsFactory.mustWatch().backdropPath,
            description = ListDetailsFactory.mustWatch().description,
            public = ListDetailsFactory.mustWatch().public,
          ),
          page = 1,
          error = BlankSlateState.Generic,
          refreshing = false,
          loadingMore = false,
        ),
      )
  }

  @Test
  fun `test initial with connection error sets offline blank slate`() {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.failure(UnknownHostException()),
      )
      .buildViewModel()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Initial(
            name = "Test List",
            backdropPath = ListDetailsFactory.mustWatch().backdropPath,
            description = ListDetailsFactory.mustWatch().description,
            public = ListDetailsFactory.mustWatch().public,
          ),
          page = 1,
          error = BlankSlateState.Offline,
          refreshing = false,
          loadingMore = false,
        ),
      )
  }
}
