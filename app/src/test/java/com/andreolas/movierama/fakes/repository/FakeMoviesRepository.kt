package com.andreolas.movierama.fakes.repository

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMoviesRepository {

    val mock: MoviesRepository = mock()

    fun mockFetchFavoriteMovies(
        response: MoviesListResult,
    ) {
        whenever(
            mock.fetchFavoriteMovies()
        ).thenReturn(
            flowOf(response)
        )
    }

    suspend fun mockFetchPopularMovies(
        request: PopularRequestApi,
        response: MoviesListResult,
    ) {
        whenever(
            mock.fetchPopularMovies(request)
        ).thenReturn(
            flowOf(response)
        )
    }

    suspend fun mockMarkAsFavorite(
        movie: PopularMovie,
        response: Result<Unit>,
    ) {
        whenever(
            mock.insertFavoriteMovie(movie)
        ).thenReturn(
            response
        )
    }

    suspend fun mockRemoveFavorite(
        id: Int,
        response: Result<Unit>,
    ) {
        whenever(
            mock.removeFavoriteMovie(id)
        ).thenReturn(
            response
        )
    }
}
