package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import com.andreolas.movierama.fakes.repository.FakeDetailsRepository
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class FakeGetMoviesDetailsUseCase : GetMovieDetailsUseCase(
    repository = FakeDetailsRepository(),
    moviesRepository = FakeMoviesRepository(),
    dispatcher = MainDispatcherRule().testDispatcher,
) {
    private var resultForMovieDetailsMap: Flow<Result<MovieDetailsResult>> = flowOf()

    fun mockResultMovieDetails(
        result: Flow<Result<MovieDetailsResult>>,
    ) {
        resultForMovieDetailsMap = result
    }

    override fun execute(parameters: DetailsRequestApi): Flow<Result<MovieDetailsResult>> {
        return resultForMovieDetailsMap
    }
}
