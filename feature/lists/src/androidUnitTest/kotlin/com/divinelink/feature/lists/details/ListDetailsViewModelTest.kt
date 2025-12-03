package com.divinelink.feature.lists.details

import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.ui.blankslate.BlankSlateState
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_batch_items_success
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_from_list_offline_error
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_remove_single_item_success
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class ListDetailsViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val robot = ListDetailsViewModelTestRobot()

  private val listDetailsRoute = Navigation.ListDetailsRoute(
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
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
            pages = mapOf(1 to ListDetailsFactory.page1().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
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
            data = ListDetailsFactory.page2(),
            pages = mapOf(
              1 to ListDetailsFactory.page1().media,
              2 to ListDetailsFactory.page2().media,
            ),
          ),
          page = 3,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
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
              data = ListDetailsFactory.page2(),
              pages = mapOf(
                1 to ListDetailsFactory.page1().media,
                2 to ListDetailsFactory.page2().media,
              ),
            ),
            page = 3,
            error = null,
            refreshing = false,
            loadingMore = false,
            snackbarMessage = null,
            selectedMedia = emptyList(),
            multipleSelectMode = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page2(),
              pages = mapOf(
                1 to ListDetailsFactory.page1().media,
                2 to ListDetailsFactory.page2().media,
              ),
            ),
            page = 3,
            error = null,
            refreshing = true,
            loadingMore = false,
            snackbarMessage = null,
            selectedMedia = emptyList(),
            multipleSelectMode = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.mustWatch(),
              pages = mapOf(
                1 to ListDetailsFactory.mustWatch().media,
              ),
            ),
            page = 2,
            error = null,
            refreshing = false,
            loadingMore = false,
            snackbarMessage = null,
            selectedMedia = emptyList(),
            multipleSelectMode = false,
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onLoadMore()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
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
            pages = mapOf(1 to ListDetailsFactory.page1().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
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
              pages = mapOf(1 to ListDetailsFactory.page1().media),
            ),
            page = 2,
            error = null,
            refreshing = false,
            loadingMore = false,
            snackbarMessage = null,
            selectedMedia = emptyList(),
            multipleSelectMode = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page1(),
              pages = mapOf(1 to ListDetailsFactory.page1().media),
            ),
            page = 2,
            error = null,
            refreshing = false,
            loadingMore = true,
            snackbarMessage = null,
            selectedMedia = emptyList(),
            multipleSelectMode = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.page2(),
              pages = mapOf(
                1 to ListDetailsFactory.page1().media,
                2 to ListDetailsFactory.page2().media,
              ),
            ),
            page = 3,
            error = null,
            refreshing = false,
            loadingMore = false,
            snackbarMessage = null,
            selectedMedia = emptyList(),
            multipleSelectMode = false,
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null, // Error should not be set when data is not initial
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
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
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
  }

  @Test
  fun `test initial with connection error sets offline blank slate`() {
    robot
      .withArgs(listDetailsRoute)
      .mockListDetails(
        Result.failure(AppException.Offline()),
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
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
  }

  @Test
  fun `test on select all items`() {
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onSelectMedia(
        ListDetailsFactory.mustWatch().media.first(),
      )
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
      .onSelectAll()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = ListDetailsFactory.mustWatch().media,
          multipleSelectMode = true,
        ),
      )
  }

  @Test
  fun `test on deselect all items`() {
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onSelectAll()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = ListDetailsFactory.mustWatch().media,
          multipleSelectMode = false,
        ),
      )
      .onDeselectAll()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
  }

  // This test does not tests that items are actually deleted, because this happens when the
  // database emits the changes. This only tests that the UI state is updated correctly.
  @Test
  fun `test on remove single selected item`() = runTest {
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onSelectMedia(
        ListDetailsFactory.mustWatch().media.first(),
      )
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
      .mockRemoveItems(Result.success(1))
      .onRemoveItems()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(
              Res.string.feature_add_to_account_remove_single_item_success,
              ListDetailsFactory.mustWatch().media.first().name,
              "Must watch",
            ),
          ),
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
  }

  @Test
  fun `test on batch remove items`() = runTest {
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onSelectMedia(ListDetailsFactory.mustWatch().media[0])
      .onSelectMedia(ListDetailsFactory.mustWatch().media[1])
      .onSelectMedia(ListDetailsFactory.mustWatch().media[2])
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = ListDetailsFactory.mustWatch().media,
          multipleSelectMode = true,
        ),
      )
      .mockRemoveItems(Result.success(3))
      .onRemoveItems()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(
              Res.string.feature_add_to_account_remove_batch_items_success,
              3,
              "Must watch",
            ),
          ),
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
  }

  @Test
  fun `test on remove selected items with 0 result`() = runTest {
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onSelectMedia(
        ListDetailsFactory.mustWatch().media.first(),
      )
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
      .mockRemoveItems(Result.success(0))
      .onRemoveItems()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
  }

  @Test
  fun `test on remove selected items with error`() = runTest {
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onSelectMedia(
        ListDetailsFactory.mustWatch().media.first(),
      )
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
      .mockRemoveItems(Result.failure(Exception("Failed to remove items")))
      .onRemoveItems()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = SnackbarMessage.from(UIText.StringText("Failed to remove items")),
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
      .onConsumeSnackbarMessage()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
  }

  @Test
  fun `test on remove selected items when offline`() = runTest {
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
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
        ),
      )
      .onSelectMedia(
        ListDetailsFactory.mustWatch().media.first(),
      )
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
      .mockRemoveItems(Result.failure(AppException.Offline()))
      .onRemoveItems()
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Data(
            data = ListDetailsFactory.mustWatch(),
            pages = mapOf(1 to ListDetailsFactory.mustWatch().media),
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(
              Res.string.feature_add_to_account_remove_from_list_offline_error,
            ),
          ),
          selectedMedia = listOf(ListDetailsFactory.mustWatch().media.first()),
          multipleSelectMode = true,
        ),
      )
  }
}
