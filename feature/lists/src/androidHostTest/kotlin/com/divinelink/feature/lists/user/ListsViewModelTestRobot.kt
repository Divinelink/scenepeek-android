package com.divinelink.feature.lists.user

import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.list.ListItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchUserListsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class ListsViewModelTestRobot : ViewModelTestRobot<ListsUiState>() {

  private val fetchListsUseCase = TestFetchUserListsUseCase()

  private lateinit var viewModel: ListsViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  override fun buildViewModel() = apply {
    viewModel = ListsViewModel(
      fetchUserListsUseCase = fetchListsUseCase.mock,
    )
  }

  override val actualUiState: Flow<ListsUiState>
    get() = viewModel.uiState

  fun onLoadMore() = apply {
    viewModel.onLoadMore()
  }

  fun onRefresh() = apply {
    viewModel.onRefresh()
  }

  fun assertUiState(expectedUiState: ListsUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedUiState)
  }

  fun mockFetchUserData(response: Result<PaginationData<ListItem>>) = apply {
    fetchListsUseCase.mockResponse(response)
  }
}
