package com.andreolas.movierama.home.domain.repository

import com.andreolas.movierama.base.data.local.popular.MovieDAO
import com.andreolas.movierama.base.data.local.popular.PersistableMovie
import com.andreolas.movierama.base.data.network.popular.ApiPopularMovie
import com.andreolas.movierama.base.data.network.popular.ApiPopularResponse
import com.andreolas.movierama.base.data.network.popular.MovieRemote
import com.andreolas.movierama.home.domain.model.PopularMovie
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdMoviesRepository @Inject constructor(
    private val movieDAO: MovieDAO,
    private val movieRemote: MovieRemote,
) : MoviesRepository {

    override fun fetchPopularMovies(page: Int): Flow<MoviesListResult> {
        return movieRemote
            .fetchPopularMovies(page)
            .map { apiResponse ->
                if (apiResponse.isNotEmpty()) {
                    Result.Success(apiResponse.first().toDomainMoviesList())
                } else {
                    Result.Error(Exception("response is empty"))
                }
            }
            .catch { exception ->
                emit(Result.Error(Exception(exception.message)))
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
        movieDAO
            .removeFavoriteMovie(id)
            .also {
                return Result.Success(it)
            }
    }
}

@JvmName("apiMovieToDomainMoviesList")
private fun ApiPopularResponse.toDomainMoviesList(): List<PopularMovie> {
    return this.results.map(ApiPopularMovie::toPopularMovie)
}

private fun ApiPopularMovie.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = this.id,
        posterPath = this.poster_path,
        releaseDate = this.release_date,
        title = this.title,
        rating = this.vote_average.toString(),
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
