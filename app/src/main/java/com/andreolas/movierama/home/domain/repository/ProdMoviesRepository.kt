package com.andreolas.movierama.home.domain.repository

import com.andreolas.movierama.base.data.local.popular.MovieDAO
import com.andreolas.movierama.base.data.local.popular.PersistableMovie
import com.andreolas.movierama.home.domain.model.PopularMovie
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdMoviesRepository @Inject constructor(
    private val movieDAO: MovieDAO,
) : MoviesRepository {

    override fun fetchPopularMovies(page: Int): Flow<MoviesListResult> {
        TODO("Not yet implemented")
    }

    override fun fetchFavoriteMovies(): Flow<MoviesListResult> {
        return movieDAO
            .fetchFavoriteMovies()
            .map { moviesList ->
                Result.Success(moviesList.toDomainMoviesList())
            }
    }
}

private fun List<PersistableMovie>.toDomainMoviesList(): List<PopularMovie> {
    return this.map(PersistableMovie::toPopularMovie)
}

private fun PersistableMovie.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = this.id,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        rating = this.rating.toString(),
        isFavorite = this.isFavorite
    )
}
