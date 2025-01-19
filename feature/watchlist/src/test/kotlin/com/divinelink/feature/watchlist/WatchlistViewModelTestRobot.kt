package com.divinelink.feature.watchlist

import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeFetchWatchlistUseCase
import com.divinelink.core.testing.usecase.TestObserveAccountUseCase
import kotlinx.coroutines.flow.Flow

class WatchlistViewModelTestRobot : ViewModelTestRobot<WatchlistUiState>() {

  private lateinit var viewModel: WatchlistViewModel

  private val observeAccountUseCase = TestObserveAccountUseCase()
  private val fetchWatchlistUseCase = FakeFetchWatchlistUseCase()

  override fun buildViewModel() = apply {
    viewModel = WatchlistViewModel(
      observeAccountUseCase = observeAccountUseCase.mock,
      fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
    )
  }

  override val actualUiState: Flow<WatchlistUiState>
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
}
