package com.andreolas.movierama.home.domain.repository

import com.andreolas.movierama.home.domain.model.PopularMovie
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow

typealias MoviesListResult = Result<List<PopularMovie>>

/**
 * The data layer for any requests related to Popular Movies.
 */
interface MoviesRepository {

    /**
     * Request popular movies using pagination.
     * Uses [Flow] in order to observe changes to our popular movies list.
     */
    fun fetchPopularMovies(
        page: Int,
    ): Flow<MoviesListResult>

    /**
     * Fetch all popular movies that the user has marked as favorite.
     */
    fun fetchFavoriteMovies(): Flow<MoviesListResult>

    /**
     * Add favorite [movie] to local storage.
     */
    suspend fun insertFavoriteMovie(
        movie: PopularMovie,
    ): Result<Unit>

    /**
     * Remove favorite [movie] from local storage.
     */
    suspend fun removeFavoriteMovie(
        id: Long,
    ): Result<Unit>
}
