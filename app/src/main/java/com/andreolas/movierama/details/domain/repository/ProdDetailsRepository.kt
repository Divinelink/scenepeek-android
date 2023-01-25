package com.andreolas.movierama.details.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.toDomainMovie
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
import com.andreolas.movierama.details.domain.model.MovieDetails
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdDetailsRepository @Inject constructor(
    private val movieRemote: MovieService,
) : DetailsRepository {

    override fun fetchMovieDetails(request: DetailsRequestApi): Flow<Result<MovieDetails>> {
        return movieRemote
            .fetchDetails(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainMovie())
            }.catch { exception ->
                flowOf(Result.Error(Exception(exception.message)))
            }
    }
}
