package com.divinelink.feature.lists.create

import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation.EditListRoute
import com.divinelink.core.network.list.model.update.UpdateListResponse
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.feature.lists.resources.feature_lists_create_failure
import com.divinelink.feature.lists.resources.feature_lists_create_successfully
import com.divinelink.feature.lists.resources.feature_lists_delete_failure
import com.divinelink.feature.lists.resources.feature_lists_delete_successfully
import com.divinelink.feature.lists.resources.feature_lists_update_failure
import com.divinelink.feature.lists.resources.feature_lists_update_successfully
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import com.divinelink.feature.lists.resources.Res as R

class CreateListViewModelTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val robot = CreateListViewModelTestRobot()

  @Test
  fun `test initialise viewModel with edit args`() {
    robot
      .withEditArgs(
        EditListRoute(
          id = 1,
          name = "Test List",
          backdropPath = "test/path",
          description = "Test Description",
          public = true,
        ),
      )
      .buildViewModel()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
        ),
      )
  }

  @Test
  fun `test initialise viewModel with edit args and null backdrop`() {
    robot
      .withEditArgs(
        EditListRoute(
          id = 1,
          name = "Test List",
          backdropPath = null,
          description = "Test Description",
          public = true,
        ),
      )
      .buildViewModel()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "",
          description = "Test Description",
          public = true,
          editMode = true,
        ),
      )
  }

  @Test
  fun `test initialise viewModel without edit args`() {
    robot
      .buildViewModel()
      .assertUiState(CreateListUiState.initial)
  }

  @Test
  fun `test name change updates ui state`() {
    robot
      .buildViewModel()
      .onNameChange("New List Name")
      .assertUiState(
        CreateListUiState.initial.copy(name = "New List Name"),
      )
  }

  @Test
  fun `test description change updates ui state`() {
    robot
      .buildViewModel()
      .onDescriptionChange("New List Description")
      .assertUiState(
        CreateListUiState.initial.copy(description = "New List Description"),
      )
  }

  @Test
  fun `test backdrop change updates ui state`() {
    robot
      .buildViewModel()
      .onBackdropChange("new/backdrop/path")
      .assertUiState(
        CreateListUiState.initial.copy(backdrop = "new/backdrop/path"),
      )
  }

  @Test
  fun `test public change updates ui state`() {
    robot
      .buildViewModel()
      .onPublicChange()
      .assertUiState(
        CreateListUiState.initial.copy(public = false),
      )
      .onPublicChange()
      .assertUiState(
        CreateListUiState.initial.copy(public = true),
      )
  }

  @Test
  fun `test on delete list with success`() = runTest {
    robot
      .withEditArgs(
        EditListRoute(
          id = 1,
          name = "Test List",
          backdropPath = "test/path",
          description = "Test Description",
          public = true,
        ),
      )
      .buildViewModel()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
        ),
      )
      .mockDeleteResponse(Result.success(Unit))
      .onDeleteList()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_lists_delete_successfully,
              "Test List",
            ),
          ),
          loading = false,

        ),
      )
      .onDismissDeleteDialog()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
          snackbarMessage = null,
          loading = false,
        ),
      )
      .verifyNavigateBackToLists()
  }

  @Test
  fun `test delete list with failure`() = runTest {
    robot
      .withEditArgs(
        EditListRoute(
          id = 1,
          name = "Test List",
          backdropPath = "test/path",
          description = "Test Description",
          public = true,
        ),
      )
      .buildViewModel()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
        ),
      )
      .mockDeleteResponse(Result.failure(Exception("Network Error")))
      .onDeleteList()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_lists_delete_failure,
              "Test List",
            ),
          ),
          loading = false,
        ),
      )
  }

  @Test
  fun `test edit list with success`() = runTest {
    robot
      .withEditArgs(
        EditListRoute(
          id = 1,
          name = "Test List",
          backdropPath = "test/path",
          description = "Test Description",
          public = true,
        ),
      )
      .buildViewModel()
      .mockEditListResponse(
        Result.success(
          UpdateListResponse(
            success = true,
            statusCode = 1,
          ),
        ),
      )
      .onCreateOrEditList()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_lists_update_successfully,
              "Test List",
            ),
          ),
          loading = false,
        ),
      )
      .verifyNavigateUp()
  }

  @Test
  fun `test edit list with failure`() = runTest {
    robot
      .withEditArgs(
        EditListRoute(
          id = 1,
          name = "Test List",
          backdropPath = "test/path",
          description = "Test Description",
          public = true,
        ),
      )
      .buildViewModel()
      .mockEditListResponse(
        Result.failure(Exception("Network Error")),
      )
      .onCreateOrEditList()
      .assertUiState(
        CreateListUiState.initial.copy(
          id = 1,
          name = "Test List",
          backdrop = "test/path",
          description = "Test Description",
          public = true,
          editMode = true,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_lists_update_failure,
              "Test List",
            ),
          ),
          loading = false,
        ),
      )
      .verifyNavigateUpNoInteraction()
  }

  @Test
  fun `test create list with success`() = runTest {
    robot
      .buildViewModel()
      .mockCreateListResponse(
        Result.success(1234),
      )
      .onNameChange("New List")
      .onDescriptionChange("Description")
      .onCreateOrEditList()
      .assertUiState(
        CreateListUiState.initial.copy(
          name = "New List",
          backdrop = "",
          description = "Description",
          public = true,
          editMode = false,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_lists_create_successfully,
              "New List",
            ),
          ),
          loading = false,
        ),
      )
      .verifyNavigateUp()
  }

  @Test
  fun `test create list with failure`() = runTest {
    robot
      .buildViewModel()
      .mockCreateListResponse(
        Result.failure(Exception("Network Error")),
      )
      .onNameChange("New List")
      .onDescriptionChange("Description")
      .onCreateOrEditList()
      .assertUiState(
        CreateListUiState.initial.copy(
          name = "New List",
          backdrop = "",
          description = "Description",
          public = true,
          editMode = false,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_lists_create_failure,
              "New List",
            ),
          ),
          loading = false,
        ),
      )
      .verifyNavigateUpNoInteraction()
  }
}
