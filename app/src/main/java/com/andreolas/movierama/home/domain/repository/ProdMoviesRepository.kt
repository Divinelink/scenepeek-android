package com.andreolas.movierama.home.domain.repository

import com.andreolas.movierama.base.data.local.popular.MovieDAO
import com.andreolas.movierama.base.data.local.popular.toDomainMoviesList
import com.andreolas.movierama.base.data.local.popular.toPersistableMovie
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.toDomainMoviesList
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.toDomainMoviesList
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
import com.andreolas.movierama.home.domain.model.PopularMovie
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdMoviesRepository @Inject constructor(
    private val movieDAO: MovieDAO,
    private val movieRemote: MovieService,
) : MoviesRepository {

    override fun fetchPopularMovies(request: PopularRequestApi): Flow<MoviesListResult> {
        return movieRemote
            .fetchPopularMovies(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainMoviesList())
            }
            .catch { exception ->
                flowOf(Result.Error(Exception(exception.message)))
            }
    }

    override fun fetchFavoriteMovies(): Flow<MoviesListResult> {
        return movieDAO
            .fetchFavoriteMovies()
            .map { moviesList ->
                Result.Success(moviesList.toDomainMoviesList())
            }
    }

    override fun fetchSearchMovies(request: SearchRequestApi): Flow<MoviesListResult> {
        return movieRemote
            .fetchSearchMovies(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainMoviesList())
            }
            .catch { exception ->
                flowOf(Result.Error(Exception(exception.message)))
            }
    }

    override suspend fun insertFavoriteMovie(movie: PopularMovie): Result<Unit> {
        movieDAO
            .insertFavoriteMovie(movie.copy(isFavorite = true).toPersistableMovie())
            .also {
                return Result.Success(it)
            }
    }

    override suspend fun removeFavoriteMovie(id: Int): Result<Unit> {
        movieDAO
            .removeFavoriteMovie(id)
            .also {
                return Result.Success(it)
            }
    }

    override suspend fun checkIfFavorite(id: Int): Result<Boolean> {
        movieDAO
            .checkIfFavorite(
                id = id,
            ).also {
                return Result.Success(it > 0)
            }
    }
}
