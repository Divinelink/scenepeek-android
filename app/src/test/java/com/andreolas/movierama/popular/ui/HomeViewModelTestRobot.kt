package com.andreolas.movierama.popular.ui

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.usecase.FakeGetFavoriteMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetPopularMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetSearchMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.usecase.SearchResult
import com.andreolas.movierama.home.ui.HomeViewModel
import com.andreolas.movierama.home.ui.HomeViewState
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTestRobot {

  private lateinit var viewModel: HomeViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeGetPopularMoviesUseCase = FakeGetPopularMoviesUseCase()
  private val fakeMarkAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val fakeGetSearchMoviesUseCase = FakeGetSearchMoviesUseCase()
  private val fakeGetFavoriteMoviesUseCase = FakeGetFavoriteMoviesUseCase()

  fun buildViewModel() = apply {
    viewModel = HomeViewModel(
      getPopularMoviesUseCase = fakeGetPopularMoviesUseCase.mock,
      getSearchMoviesUseCase = fakeGetSearchMoviesUseCase.mock,
      getFavoriteMoviesUseCase = fakeGetFavoriteMoviesUseCase.mock,
      markAsFavoriteUseCase = fakeMarkAsFavoriteUseCase.mock,
    )
  }

  fun assertViewState(
    expectedViewState: HomeViewState,
  ) = apply {
    assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
  }

  fun assertFalseViewState(
    expectedViewState: HomeViewState,
  ) = apply {
    assertThat(viewModel.viewState.value).isNotEqualTo(expectedViewState)
  }

  fun mockFetchPopularMovies(
    response: MoviesListResult,
  ) = apply {
    fakeGetPopularMoviesUseCase.mockFetchPopularMovies(
      response = response,
    )
  }

  fun mockFetchFavoriteMovies(
    response: MoviesListResult,
  ) = apply {
    fakeGetFavoriteMoviesUseCase.mockGetFavoriteMovies(
      response = response,
    )
  }

  fun mockFetchSearchMovies(
    response: Result<SearchResult>,
  ) = apply {
    fakeGetSearchMoviesUseCase.mockFetchSearchMovies(
      response = response,
    )
  }

  suspend fun mockMarkAsFavorite(
    result: Result<Unit>,
  ) = apply {
    fakeMarkAsFavoriteUseCase.mockMarkAsFavoriteResult(
      result = result,
    )
  }

  fun onLoadNextPage() = apply {
    viewModel.onLoadNextPage()
  }

  fun onMovieClicked(movie: PopularMovie) = apply {
    viewModel.onMovieClicked(movie)
  }

  fun onSwipeDown() = apply {
    viewModel.onSwipeDown()
  }

  fun onMarkAsFavorite(movie: PopularMovie) = apply {
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

  fun onFilterClicked(filter: String) = apply {
    viewModel.onFilterClicked(filter)
  }

  suspend fun delay(
    timeInMillis: Long,
  ) = apply {
    kotlinx.coroutines.delay(timeInMillis)
  }
}
