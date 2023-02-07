package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class FakeGetPopularMoviesUseCase : GetPopularMoviesUseCase(
    moviesRepository = FakeMoviesRepository(),
    dispatcher = MainDispatcherRule().testDispatcher,
) {
    private var resultForPopularMovies: Flow<MoviesListResult> = flowOf()

    fun mockFetchPopularMovies(
        result: Flow<MoviesListResult>,
    ) {
        resultForPopularMovies = result
    }

    override fun execute(parameters: PopularRequestApi): Flow<MoviesListResult> {
        return resultForPopularMovies
    }
}
