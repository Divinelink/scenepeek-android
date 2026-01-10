package com.divinelink.scenepeek.popular.ui

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.core.domain.search.SearchStateManager
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.core.testing.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.core.ui.components.Filter
import com.divinelink.feature.home.HomeUiState
import com.divinelink.feature.home.HomeViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class HomeViewModelTestRobot {

  private lateinit var viewModel: HomeViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeGetPopularMoviesUseCase = FakeGetPopularMoviesUseCase()
  private val fakeMarkAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
  private val fakeGetFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

  fun buildViewModel() = apply {
    viewModel = HomeViewModel(
      getPopularMoviesUseCase = fakeGetPopularMoviesUseCase.mock,
      getFavoriteMoviesUseCase = fakeGetFavoriteMoviesUseCase.mock,
      markAsFavoriteUseCase = fakeMarkAsFavoriteUseCase,
      searchStateManager = SearchStateManager(),
    )
  }

  fun assertViewState(expectedViewState: HomeUiState) = apply {
    assertThat(viewModel.uiState.value).isEqualTo(expectedViewState)
  }

  fun mockFetchPopularMovies(response: MediaListResult) = apply {
    fakeGetPopularMoviesUseCase.mockFetchPopularMovies(
      response = response,
    )
  }

  fun mockFetchFavoriteMovies(response: MediaListResult) = apply {
    fakeGetFavoriteMoviesUseCase.mockGetFavoriteMovies(
      response = response,
    )
  }

  fun mockMarkAsFavorite(
    mediaItem: MediaItem.Media,
    result: Result<Boolean>,
  ) = apply {
    fakeMarkAsFavoriteUseCase.mockMarkAsFavoriteResult(
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
    viewModel.onMarkAsFavoriteClicked(movie)
  }

  fun onClearFiltersClicked() = apply {
    viewModel.onClearFiltersClicked()
  }

  fun onFilterClicked(filter: Filter) = apply {
    viewModel.onFilterClick(filter)
  }
}
