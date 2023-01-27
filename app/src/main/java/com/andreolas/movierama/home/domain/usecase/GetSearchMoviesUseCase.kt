package com.andreolas.movierama.home.domain.usecase

import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetSearchMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<SearchRequestApi, List<PopularMovie>>(dispatcher) {

    override fun execute(
        parameters: SearchRequestApi,
    ): Flow<MoviesListResult> {
        val searchMovies = moviesRepository.fetchSearchMovies(parameters)

        return searchMovies.map { result ->
            when (result) {
                is Result.Success -> {
                    result.data.map { movie ->
                        if (moviesRepository.checkIfFavorite(movie.id).data == true) {
                            movie.copy(isFavorite = true)
                        } else {
                            movie
                        }
                    }.run { Result.Success(this) }
                }
                is Result.Error -> result
                Result.Loading -> result
            }
        }
    }
}
