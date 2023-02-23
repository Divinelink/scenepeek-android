package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class FakeGetFavoriteMoviesUseCase : GetFavoriteMoviesUseCase(
    moviesRepository = FakeMoviesRepository(),
    dispatcher = MainDispatcherRule().testDispatcher,
) {
    private var resultForPopularMovies: Flow<Result<List<PopularMovie>>> = flowOf()

    fun mockFetchFavoriteMovies(
        result: Flow<Result<List<PopularMovie>>>,
    ) {
        resultForPopularMovies = result
    }

    override fun execute(parameters: Unit): Flow<Result<List<PopularMovie>>> {
        return resultForPopularMovies
    }
}
