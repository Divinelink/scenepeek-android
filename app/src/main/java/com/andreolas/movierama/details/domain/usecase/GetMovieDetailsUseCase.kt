package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.base.data.local.popular.MovieDAO
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.di.IoDispatcher
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import gr.divinelink.core.util.domain.FlowUseCase
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetMovieDetailsUseCase @Inject constructor(
    private val repository: DetailsRepository,
    private val movieDAO: MovieDAO,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : FlowUseCase<DetailsRequestApi, MovieDetails>(dispatcher) {

    override fun execute(parameters: DetailsRequestApi): Flow<Result<MovieDetails>> {
        val movieDetails = repository.fetchMovieDetails(
            request = parameters,
        )

        return movieDetails.map { result ->
            when (result) {
                is Result.Success -> {
                    val details = if (movieDAO.checkIfFavorite(result.data.id) == 1) {
                        result.data.copy(isFavorite = true)
                    } else {
                        result.data
                    }
                    Result.Success(details)
                }
                is Result.Error -> result
                Result.Loading -> result
            }
        }
    }
}
