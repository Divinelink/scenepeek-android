package com.andreolas.movierama.fakes.repository

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMoviesRepository : MoviesRepository {

    lateinit var popularMoviesResult: Flow<MoviesListResult>

    var favoriteMoviesResult: Flow<MoviesListResult> = flowOf()

    val checkIfFavoriteResult: MutableMap<Int, Result<Boolean>> = mutableMapOf()

    override fun fetchPopularMovies(request: PopularRequestApi): Flow<MoviesListResult> {
        return popularMoviesResult
    }

    override fun fetchFavoriteMovies(): Flow<MoviesListResult> {
        return favoriteMoviesResult
    }

    override fun fetchFavoriteMoviesIds(): Flow<Result<List<Int>>> {
        TODO("Not yet implemented")
    }

    override fun fetchSearchMovies(request: SearchRequestApi): Flow<MoviesListResult> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavoriteMovie(movie: PopularMovie): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavoriteMovie(id: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIfFavorite(id: Int): Result<Boolean> {
        return checkIfFavoriteResult[id]!!
    }
}
