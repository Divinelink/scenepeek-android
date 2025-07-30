package com.divinelink.feature.add.to.account.list

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.navigation.route.Navigation.AddToListRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestAddItemToListUseCase
import com.divinelink.core.testing.usecase.TestFetchUserListsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class AddToListViewModelTestRobot : ViewModelTestRobot<AddToListUiState>() {

  private val fetchUserListsUseCase = TestFetchUserListsUseCase()
  private val addItemToListUseCase = TestAddItemToListUseCase()

  private lateinit var viewModel: AddToListViewModel
  private lateinit var navArgs: AddToListRoute

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = AddToListViewModel(
      fetchUserListsUseCase = fetchUserListsUseCase.mock,
      addItemToListUseCase = addItemToListUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
          "mediaType" to navArgs.mediaType,
        ),
      ),
    )
  }

  fun withArgs(args: AddToListRoute) = apply {
    this.navArgs = args
  }

  override val actualUiState: Flow<AddToListUiState>
    get() = viewModel.uiState

  fun onLoadMore() = apply {
    viewModel.onAction(AddToListAction.LoadMore)
  }

  fun onConsumeDisplayMessage() = apply {
    viewModel.onAction(AddToListAction.ConsumeDisplayMessage)
  }

  fun onListClick(id: Int) = apply {
    viewModel.onAction(AddToListAction.OnListClick(id = id))
  }

  fun onLogin() = apply {
    viewModel.onAction(AddToListAction.Login)
  }

  fun assertUiState(expectedUiState: AddToListUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  suspend fun expectNoNavigateToTMDBAuth() = apply {
    viewModel.navigateToTMDBAuth.test {
      expectNoEvents()
    }
  }

  suspend fun awaitNavigateToTMDBAuth() = apply {
    viewModel.navigateToTMDBAuth.test {
      awaitItem()
    }
  }

  fun mockFetchUserLists(response: Result<PaginationData<ListItem>>) = apply {
    fetchUserListsUseCase.mockResponse(response)
  }

  fun mockFetchUserLists(response: Channel<Result<PaginationData<ListItem>>>) = apply {
    fetchUserListsUseCase.mockResponse(response)
  }

  fun mockAddItemToList(response: Result<Boolean>) = apply {
    addItemToListUseCase.mockResponse(response)
  }
}
