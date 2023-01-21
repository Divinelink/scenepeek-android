package com.andreolas.movierama.fakes.dao

import com.andreolas.movierama.base.data.network.popular.ApiPopularResponse
import com.andreolas.movierama.base.data.network.popular.MovieRemote
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMovieRemote {

    val mock: MovieRemote = mock()

    fun mockFetchPopularMovies(
        page: Int,
        result: Flow<List<ApiPopularResponse>>,
    ) {
        whenever(
            mock.fetchPopularMovies(page)
        ).thenReturn(
            result
        )
    }
}
