package com.andreolas.movierama.home.domain.repository

import com.andreolas.movierama.base.data.local.popular.MovieDAO
import com.andreolas.movierama.base.data.local.popular.PersistableMovie
import com.andreolas.movierama.base.data.remote.popular.MovieService
import com.andreolas.movierama.base.data.remote.popular.dto.PopularMovieApi
import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.base.data.remote.popular.dto.PopularResponseApi
import com.andreolas.movierama.base.data.remote.popular.service.MovieService
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

    override suspend fun fetchPopularMovies(request: PopularRequestApi): Flow<MoviesListResult> {
        return movieRemote
            .fetchPopularMovies(request)
            .map { apiResponse ->
                Result.Success(apiResponse.toDomainMoviesList())
            }
            .catch { exception ->
                flowOf(Result.Error(Exception(exception.message)))
                // .flatMapConcat {
                // movieRemote.fetchPopularMovies(request)
                // }
            }
    }

    override fun fetchFavoriteMovies(): Flow<MoviesListResult> {
        return movieDAO
            .fetchFavoriteMovies()
            .map { moviesList ->
                Result.Success(moviesList.toDomainMoviesList())
            }
    }

    override suspend fun insertFavoriteMovie(movie: PopularMovie): Result<Unit> {
        movieDAO
            .insertFavoriteMovie(movie.toPersistableMovie())
            .also {
                return Result.Success(it)
            }
    }

    override suspend fun removeFavoriteMovie(id: Long): Result<Unit> {
    override suspend fun removeFavoriteMovie(id: Int): Result<Unit> {
        movieDAO
            .removeFavoriteMovie(id)
            .also {
                return Result.Success(it)
            }
    }
}

@JvmName("apiMovieToDomainMoviesList")
private fun PopularResponseApi.toDomainMoviesList(): List<PopularMovie> {
    return this.results.map(PopularMovieApi::toPopularMovie)
}

private fun PopularMovieApi.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = this.id,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        rating = this.voteAverage.toString(),
        isFavorite = false,
    )
}

@JvmName("persistableMovieToDomainMoviesList")
private fun List<PersistableMovie>.toDomainMoviesList(): List<PopularMovie> {
    return this.map(PersistableMovie::toPopularMovie)
}

private fun PersistableMovie.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = this.id,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        rating = this.rating,
        isFavorite = this.isFavorite
    )
}

private fun PopularMovie.toPersistableMovie(): PersistableMovie {
    return PersistableMovie(
        id = this.id,
        title = this.title,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        rating = this.rating,
        isFavorite = this.isFavorite
    )
}
