package com.divinelink.feature.watchlist

import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeFetchWatchlistUseCase
import com.divinelink.core.testing.usecase.FakeObserveSessionUseCase
import kotlinx.coroutines.flow.Flow

class WatchlistViewModelTestRobot : ViewModelTestRobot<WatchlistUiState>() {

  private lateinit var viewModel: WatchlistViewModel

  private val observeSessionUseCase = FakeObserveSessionUseCase()
  private val fetchWatchlistUseCase = FakeFetchWatchlistUseCase()

  override fun buildViewModel() = apply {
    viewModel = WatchlistViewModel(
      observeSessionUseCase = observeSessionUseCase.mock,
      fetchWatchlistUseCase = fetchWatchlistUseCase.mock,
    )
  }

  override val actualUiState: Flow<WatchlistUiState>
    get() = viewModel.uiState

  fun mockObserveSession(response: FakeObserveSessionUseCase.() -> Unit) = apply {
    observeSessionUseCase.response()
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
