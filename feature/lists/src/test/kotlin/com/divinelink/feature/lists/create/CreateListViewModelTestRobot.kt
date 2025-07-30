package com.divinelink.feature.lists.create

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.divinelink.core.navigation.route.Navigation.EditListRoute
import com.divinelink.core.network.list.model.update.UpdateListResponse
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.repository.TestListRepository
import com.divinelink.core.testing.usecase.TestCreateListUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class CreateListViewModelTestRobot : ViewModelTestRobot<CreateListUiState>() {

  private lateinit var viewModel: CreateListViewModel
  private lateinit var editNavArgs: EditListRoute

  private val createListUseCase = TestCreateListUseCase()
  private val repository = TestListRepository()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = CreateListViewModel(
      createListUseCase = createListUseCase.mock,
      repository = repository.mock,
      savedStateHandle = if (::editNavArgs.isInitialized) {
        SavedStateHandle(
          mapOf(
            "id" to editNavArgs.id,
            "name" to editNavArgs.name,
            "backdropPath" to editNavArgs.backdropPath,
            "description" to editNavArgs.description,
            "public" to editNavArgs.public,
          ),
        )
      } else {
        SavedStateHandle()
      },

    )
  }

  override val actualUiState: Flow<CreateListUiState>
    get() = viewModel.uiState

  fun assertUiState(expectedUiState: CreateListUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  fun withEditArgs(args: EditListRoute) = apply {
    this.editNavArgs = args
  }

  fun onNameChange(name: String) = apply {
    viewModel.onAction(CreateListAction.NameChanged(name))
  }

  fun onDescriptionChange(description: String) = apply {
    viewModel.onAction(CreateListAction.DescriptionChanged(description))
  }

  fun onBackdropChange(backdrop: String) = apply {
    viewModel.onAction(CreateListAction.BackdropChanged(backdrop))
  }

  fun onPublicChange() = apply {
    viewModel.onAction(CreateListAction.PublicChanged)
  }

  fun onCreateOrEditList() = apply {
    viewModel.onAction(CreateListAction.CreateOrEditList)
  }

  fun onDeleteList() = apply {
    viewModel.onAction(CreateListAction.DeleteList)
  }

  fun onDismissDeleteDialog() = apply {
    viewModel.onAction(CreateListAction.DismissSnackbar)
  }

  suspend fun verifyNavigateUp() = apply {
    viewModel.onNavigateUp.test {
      assertThat(awaitItem()).isEqualTo(Unit)
    }
  }

  suspend fun verifyNavigateUpNoInteraction() = apply {
    viewModel.onNavigateUp.test {
      expectNoEvents()
    }
  }

  suspend fun verifyNavigateBackToLists() = apply {
    viewModel.onNavigateBackToLists.test {
      assertThat(awaitItem()).isEqualTo(Unit)
    }
  }

  /**
   * Mocks
   */
  suspend fun mockDeleteResponse(result: Result<Unit>) = apply {
    repository.mockDeleteListResponse(result)
  }

  suspend fun mockEditListResponse(result: Result<UpdateListResponse>) = apply {
    repository.mockEditListResponse(result)
  }

  fun mockCreateListResponse(result: Result<Int>) = apply {
    createListUseCase.mockSuccess(result)
  }
}
