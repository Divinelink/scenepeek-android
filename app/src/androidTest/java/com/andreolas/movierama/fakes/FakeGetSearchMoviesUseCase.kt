package com.andreolas.movierama.fakes

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.SearchResult
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class FakeGetSearchMoviesUseCase : GetSearchMoviesUseCase(
    moviesRepository = FakeMoviesRepository(),
    dispatcher = MainDispatcherRule().testDispatcher,
) {
    private var resultForSearchMovies: Flow<Result<SearchResult>> = flowOf()

    fun mockFetchSearchMovies(
        result: Flow<Result<SearchResult>>,
    ) {
        resultForSearchMovies = result
    }

    override fun execute(parameters: SearchRequestApi): Flow<Result<SearchResult>> {
        return resultForSearchMovies
    }
}
