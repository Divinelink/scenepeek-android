package com.divinelink.feature.lists.details

import com.divinelink.core.fixtures.model.account.AccountDetailsFactory
import com.divinelink.core.fixtures.model.list.ListDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.list.details.ListDetailsData
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.network.Resource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.RoboTest
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

class ListDetailsViewModelTest : RoboTest() {

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
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test refresh correctly clears pages`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.page1()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.page2()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
            canEdit = true,
            sortOption = null,
            updatingSort = false,
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
            canEdit = true,
            sortOption = null,
            updatingSort = false,
          ),
          ListDetailsUiState(
            id = 1,
            details = ListDetailsData.Data(
              data = ListDetailsFactory.mustWatch(),
              pages = mapOf(
                1 to ListDetailsFactory.mustWatch().media,
                2 to ListDetailsFactory.page2().media,
              ),
            ),
            page = 2,
            error = null,
            refreshing = false,
            loadingMore = false,
            snackbarMessage = null,
            selectedMedia = emptyList(),
            multipleSelectMode = false,
            canEdit = true,
            sortOption = null,
            updatingSort = false,
          ),
        ),
      )
  }

  @Test
  fun `test load more when cannot load more`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test load more correctly sets loading more`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.page1()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.page2()),
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
            canEdit = true,
            sortOption = null,
            updatingSort = false,
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
            canEdit = true,
            sortOption = null,
            updatingSort = false,
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
            canEdit = true,
            sortOption = null,
            updatingSort = false,
          ),
        ),
      )
  }

  @Test
  fun `test load more with error when data not initial does not set error`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(Resource.Success(ListDetailsFactory.mustWatch()))
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Error(Exception("Foo")),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test initial with error`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Error(Exception("Foo")),
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
          canEdit = false,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test initial with connection error sets offline blank slate`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Error(AppException.Offline()),
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
          canEdit = false,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test on select all items`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test on deselect all items`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  // This test does not tests that items are actually deleted, because this happens when the
  // database emits the changes. This only tests that the UI state is updated correctly.
  @Test
  fun `test on remove single selected item`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test on batch remove items`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test on remove selected items with 0 result`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test on remove selected items with error`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test on remove selected items when offline`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(
        Resource.Success(ListDetailsFactory.mustWatch()),
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test update sorting option clears all data`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(Resource.Success(ListDetailsFactory.mustWatch()))
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
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
      .mockListDetails(Resource.Success(ListDetailsFactory.page1()))
      .updateSortBy(1, SortBy.Title)
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Initial(
            name = "Must watch",
            backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
            description = "A list of must watch movies and shows",
            public = false,
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
          canEdit = true,
          sortOption = SortOption(
            sortBy = SortBy.Title,
            direction = SortDirection.DESC,
          ),
          updatingSort = false,
        ),
      )
      .updateSortDirection(1)
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Initial(
            name = "Must watch",
            backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
            description = "A list of must watch movies and shows",
            public = false,
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
          canEdit = true,
          sortOption = SortOption(
            sortBy = SortBy.Title,
            direction = SortDirection.ASC,
          ),
          updatingSort = false,
        ),
      )
  }

  @Test
  fun `test clear sorting option`() = runTest {
    robot
      .withArgs(listDetailsRoute)
      .mockTMDBAccount(AccountDetailsFactory.elsa)
      .mockListDetails(Resource.Success(ListDetailsFactory.mustWatch()))
      .buildViewModel()
      .mockListDetails(Resource.Success(ListDetailsFactory.page1()))
      .updateSortBy(1, SortBy.Title)
      .updateSortDirection(1)
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Initial(
            name = "Must watch",
            backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
            description = "A list of must watch movies and shows",
            public = false,
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
          canEdit = true,
          sortOption = SortOption(
            sortBy = SortBy.Title,
            direction = SortDirection.ASC,
          ),
          updatingSort = false,
        ),
      )
      .clearSortOption(listId = 1)
      .assertUiState(
        ListDetailsUiState(
          id = 1,
          details = ListDetailsData.Initial(
            name = "Must watch",
            backdropPath = "/layPSOJGckJv3PXZDIVluMq69mn.jpg",
            description = "A list of must watch movies and shows",
            public = false,
          ),
          page = 2,
          error = null,
          refreshing = false,
          loadingMore = false,
          snackbarMessage = null,
          selectedMedia = emptyList(),
          multipleSelectMode = false,
          canEdit = true,
          sortOption = null,
          updatingSort = false,
        ),
      )
  }
}
