package com.andreolas.movierama.fakes.repository

import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchRequestApi
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
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

    fun mockFetchFavoriteMoviesIds(
        response: Result<List<Int>>,
    ) {
        whenever(
            mock.fetchFavoriteMediaIDs()
        ).thenReturn(
            flowOf(response)
        )
    }

    fun mockFetchPopularMovies(
        request: PopularRequestApi,
        response: MoviesListResult,
    ) {
        whenever(
            mock.fetchPopularMovies(request)
        ).thenReturn(
            flowOf(response)
        )
    }

    fun mockFetchSearchMovies(
        request: SearchRequestApi,
        response: MoviesListResult,
    ) {
        whenever(
            mock.fetchSearchMovies(request)
        ).thenReturn(
            flowOf(response)
        )
    }

    suspend fun mockMarkAsFavorite(
        movie: PopularMovie,
        response: Result<Unit>,
    ) {
        whenever(
            mock.insertFavoriteMedia(movie)
        ).thenReturn(
            response
        )
    }

    suspend fun mockCheckFavorite(
        id: Int,
        response: Result<Boolean>,
    ) {
        whenever(
            mock.checkIfMediaIsFavorite(id)
        ).thenReturn(response)
    }

    suspend fun verifyCheckIsFavorite(
        response: Result<Boolean>,
    ) {
        whenever(
            mock.checkIfMediaIsFavorite(any())
        ).thenReturn(response)
    }

    suspend fun mockRemoveFavorite(
        id: Int,
        response: Result<Unit>,
    ) {
        whenever(
            mock.removeFavoriteMedia(id)
        ).thenReturn(
            response
        )
    }
}
