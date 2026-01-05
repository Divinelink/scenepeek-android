package com.divinelink.feature.search

import com.divinelink.core.domain.search.MultiSearchResult
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.feature.search.ui.SearchUiState
import com.divinelink.feature.search.ui.SearchViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class SearchViewModelTestRobot {

  private lateinit var viewModel: SearchViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()

  fun buildViewModel() = apply {
    viewModel = SearchViewModel(
      fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
      searchStateManager = SearchStateManager(),
    )
  }

  fun assertUiState(expectedViewState: SearchUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedViewState)
  }

  fun mockFetchSearchMedia(response: Result<MultiSearchResult>) = apply {
    fetchMultiInfoSearchUseCase.mockFetchMultiInfoSearch(
      response = response,
    )
  }

  fun onRetryClick() = apply {
    viewModel.onRetryClick()
  }

  fun onLoadNextPage() = apply {
    viewModel.onLoadNextPage()
  }

  fun onSearch(query: String, reset: Boolean = false) = apply {
    viewModel.onSearch(
      query = query,
      reset = reset,
    )
  }

  fun onClearClicked() = apply {
    viewModel.onClearClick()
  }

  fun onSelectTab(tab: SearchTab) = apply {
    viewModel.onSelectTab(tab)
  }

  suspend fun delay(timeInMillis: Long) = apply {
    kotlinx.coroutines.delay(timeInMillis)
  }
}
