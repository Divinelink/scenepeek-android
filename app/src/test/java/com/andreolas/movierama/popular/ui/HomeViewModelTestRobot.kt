package com.andreolas.movierama.popular.ui

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.usecase.FakeGetPopularMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeGetSearchMoviesUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.fakes.usecase.FakeRemoveFavoriteUseCase
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
    private val fakeRemoveFavoriteUseCase = FakeRemoveFavoriteUseCase()
    private val fakeGetSearchMoviesUseCase = FakeGetSearchMoviesUseCase()

    fun buildViewModel() = apply {
        viewModel = HomeViewModel(
            getPopularMoviesUseCase = fakeGetPopularMoviesUseCase.mock,
            markAsFavoriteUseCase = fakeMarkAsFavoriteUseCase.mock,
            getSearchMoviesUseCase = fakeGetSearchMoviesUseCase.mock,
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

    suspend fun mockRemoveFavorite(
        result: Result<Unit>,
    ) = apply {
        fakeRemoveFavoriteUseCase.mockRemoveFavoriteResult(
            result = result,
        )
    }

    fun onLoadNextPage() = apply {
        viewModel.onLoadNextPage()
    }

    fun onMovieClicked(movie: PopularMovie) = apply {
        viewModel.onMovieClicked(movie)
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

    suspend fun delay(
        timeInMillis: Long,
    ) = apply {
        kotlinx.coroutines.delay(timeInMillis)
    }
}
