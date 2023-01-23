package com.andreolas.movierama.popular.ui

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.andreolas.movierama.home.domain.usecase.RemoveFavoriteUseCase
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
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private val fakeMoviesRepository = FakeMoviesRepository()

    fun buildViewModel() = apply {
        viewModel = HomeViewModel(
            getPopularMoviesUseCase = GetPopularMoviesUseCase(
                moviesRepository = fakeMoviesRepository.mock,
                dispatcher = testDispatcher,
            ),
            markAsFavoriteUseCase = MarkAsFavoriteUseCase(
                repository = fakeMoviesRepository.mock,
                dispatcher = testDispatcher,
            ),
            removeFavoriteUseCase = RemoveFavoriteUseCase(
                repository = fakeMoviesRepository.mock,
                dispatcher = testDispatcher,
            )
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

    suspend fun mockFetchPopularMovies(
        request: PopularRequestApi,
        response: MoviesListResult,
    ) = apply {
        fakeMoviesRepository.mockFetchPopularMovies(
            request = request,
            response = response,
        )
    }

    fun mockFetchFavoriteMovies(
        response: MoviesListResult,
    ) = apply {
        fakeMoviesRepository.mockFetchFavoriteMovies(
            response = response,
        )
    }

    suspend fun mockMarkAsFavorite(
        movie: PopularMovie,
        response: Result<Unit>,
    ) = apply {
        fakeMoviesRepository.mockMarkAsFavorite(
            movie = movie,
            response = response,
        )
    }

    suspend fun mockRemoveFavorite(
        id: Int,
        response: Result<Unit>,
    ) = apply {
        fakeMoviesRepository.mockRemoveFavorite(
            id = id,
            response = response,
        )
    }

    fun onLoadNextPage() = apply {
        viewModel.onLoadNextPage()
    }

    fun onMovieClicked(movie: PopularMovie) = apply {
        viewModel.onMovieClicked(movie)
    }
}
