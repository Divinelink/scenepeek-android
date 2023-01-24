package com.andreolas.movierama.fakes.dao

import com.andreolas.movierama.base.data.remote.dto.PopularRequestApi
import com.andreolas.movierama.base.data.remote.dto.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMovieRemote {

    val mock: MovieService = mock()

    suspend fun mockFetchPopularMovies(
        request: PopularRequestApi,
        result: Flow<PopularResponseApi>,
    ) {
        whenever(
            mock.fetchPopularMovies(request)
        ).thenReturn(
            result
        )
    }

//    suspend fun mockErrorFetchPopularMovies(
//        request: PopularRequestApi,
//        exception: Flow<PopularResponseApi>,
//    ) {
//        whenever(
//            mock.fetchPopularMovies(request)
//        ).thenReturn(
//            exception
//        )
//    }
}
