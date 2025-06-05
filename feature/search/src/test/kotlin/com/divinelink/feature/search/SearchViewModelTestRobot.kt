package com.divinelink.feature.search

import com.divinelink.core.domain.search.MultiSearchResult
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.feature.search.ui.SearchUiState
import com.divinelink.feature.search.ui.SearchViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class SearchViewModelTestRobot {

  private lateinit var viewModel: SearchViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val markAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
  private val fetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()

  fun buildViewModel() = apply {
    viewModel = SearchViewModel(
      fetchMultiInfoSearchUseCase = fetchMultiInfoSearchUseCase.mock,
      markAsFavoriteUseCase = markAsFavoriteUseCase,
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

  fun mockMarkAsFavorite(
    mediaItem: MediaItem.Media,
    result: Result<Unit>,
  ) = apply {
    markAsFavoriteUseCase.mockMarkAsFavoriteResult(
      media = mediaItem,
      result = result,
    )
  }

  fun onRetryClick() = apply {
    viewModel.onRetryClick()
  }

  fun onLoadNextPage() = apply {
    viewModel.onLoadNextPage()
  }

  fun onMarkAsFavorite(movie: MediaItem.Media) = apply {
    viewModel.onMarkAsFavoriteClick(movie)
  }

  fun onSearchMovies(query: String) = apply {
    viewModel.onSearchMovies(query)
  }

  fun onClearClicked() = apply {
    viewModel.onClearClick()
  }

  suspend fun delay(timeInMillis: Long) = apply {
    kotlinx.coroutines.delay(timeInMillis)
  }
}
