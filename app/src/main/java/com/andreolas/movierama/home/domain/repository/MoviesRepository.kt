package com.andreolas.movierama.home.domain.repository

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchRequestApi
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
        request: PopularRequestApi,
    ): Flow<MoviesListResult>

    /**
     * Fetch all popular movies that the user has marked as favorite.
     */
    fun fetchFavoriteMovies(): Flow<MoviesListResult>

    /**
     * Fetch all favorite movie ids.
     * Uses [Flow] in order to observe changes to our favorite movies list.
     */
    fun fetchFavoriteMoviesIds(): Flow<Result<List<Int>>>

    /**
     * Request movies through a search query. Uses pagination.
     * Uses [Flow] in order to observe changes to our movies list.
     */
    fun fetchSearchMovies(
        request: SearchRequestApi,
    ): Flow<MoviesListResult>

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
        id: Int,
    ): Result<Unit>

    suspend fun checkIfFavorite(
        id: Int,
    ): Result<Boolean>
}
