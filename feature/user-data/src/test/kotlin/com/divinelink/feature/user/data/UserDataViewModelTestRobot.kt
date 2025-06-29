package com.divinelink.feature.user.data

import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeFetchWatchlistUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import kotlinx.coroutines.flow.Flow

class UserDataViewModelTestRobot : ViewModelTestRobot<UserDataUiState>() {

  private lateinit var viewModel: UserDataViewModel

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchWatchlistUseCase = FakeFetchWatchlistUseCase()

  override fun buildViewModel() = apply {
    viewModel = UserDataViewModel(
      observeAccountUseCase = observeAccountUseCase.mock,
      fetchUserDataUseCase = fetchWatchlistUseCase.mock,
    )
  }

  override val actualUiState: Flow<UserDataUiState>
    get() = viewModel.uiState

  fun mockObserveAccount(response: TestObserveAccountUseCase.() -> Unit) = apply {
    observeAccountUseCase.response()
  }

  fun mockFetchWatchlist(response: FakeFetchWatchlistUseCase.() -> Unit) = apply {
    fetchWatchlistUseCase.response()
  }

  fun onLoadMore() = apply {
    viewModel.onLoadMore()
  }

  fun selectTab(tabIndex: Int) = apply {
    viewModel.onTabSelected(tabIndex)
  }

  fun onRefresh() = apply {
    viewModel.onRefresh()
  }
}
