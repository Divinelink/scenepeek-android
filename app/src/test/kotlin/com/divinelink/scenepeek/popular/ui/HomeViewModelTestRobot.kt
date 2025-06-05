package com.divinelink.scenepeek.popular.ui

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.core.domain.search.MultiSearchResult
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.ui.components.Filter
import com.divinelink.scenepeek.fakes.usecase.FakeFetchMultiInfoSearchUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeGetPopularMoviesUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.divinelink.scenepeek.home.ui.HomeViewModel
import com.divinelink.scenepeek.home.ui.HomeViewState
import com.google.common.truth.Truth.assertThat
import org.junit.Rule

class HomeViewModelTestRobot {

  private lateinit var viewModel: HomeViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeGetPopularMoviesUseCase = FakeGetPopularMoviesUseCase()
  private val fakeMarkAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val fakeGetFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()
  private val fakeFetchMultiInfoSearchUseCase = FakeFetchMultiInfoSearchUseCase()

  fun buildViewModel() = apply {
    viewModel = HomeViewModel(
      getPopularMoviesUseCase = fakeGetPopularMoviesUseCase.mock,
      fetchMultiInfoSearchUseCase = fakeFetchMultiInfoSearchUseCase.mock,
      getFavoriteMoviesUseCase = fakeGetFavoriteMoviesUseCase.mock,
      markAsFavoriteUseCase = fakeMarkAsFavoriteUseCase,
    )
  }

  fun assertViewState(expectedViewState: HomeViewState) = apply {
    assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
  }

  fun assertFalseViewState(expectedViewState: HomeViewState) = apply {
    assertThat(viewModel.viewState.value).isNotEqualTo(expectedViewState)
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

  fun mockFetchSearchMedia(response: Result<MultiSearchResult>) = apply {
    fakeFetchMultiInfoSearchUseCase.mockFetchMultiInfoSearch(
      response = response,
    )
  }

  fun mockMarkAsFavorite(
    mediaItem: MediaItem.Media,
    result: Result<Unit>,
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

  fun onSearchMovies(query: String) = apply {
    viewModel.onSearchMovies(query)
  }

  fun onClearClicked() = apply {
    viewModel.onClearClicked()
  }

  fun onClearFiltersClicked() = apply {
    viewModel.onClearFiltersClicked()
  }

  fun onFilterClicked(filter: Filter) = apply {
    viewModel.onFilterClick(filter)
  }

  suspend fun delay(timeInMillis: Long) = apply {
    kotlinx.coroutines.delay(timeInMillis)
  }
}
