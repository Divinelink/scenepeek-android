package com.andreolas.movierama.details.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetails
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow

/**
 * The data layer for any requests related to Movie Details Movies.
 */
interface DetailsRepository {

    fun fetchMovieDetails(
        request: DetailsRequestApi,
    ): Flow<Result<MovieDetails>>
}
