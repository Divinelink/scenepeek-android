package com.andreolas.movierama.fakes.remote

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchResponseApi
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMovieRemote {

    val mock: MovieService = mock()

    fun mockFetchPopularMovies(
        request: PopularRequestApi,
        result: Flow<PopularResponseApi>,
    ) {
        whenever(
            mock.fetchPopularMovies(request)
        ).thenReturn(
            result
        )
    }

    fun mockFetchSearchMovies(
        request: SearchRequestApi,
        result: Flow<SearchResponseApi>,
    ) {
        whenever(
            mock.fetchSearchMovies(request)
        ).thenReturn(
            result
        )
    }

    fun mockFetchMovieDetails(
        request: DetailsRequestApi,
        response: Flow<DetailsResponseApi>,
    ) {
        whenever(
            mock.fetchDetails(request)
        ).thenReturn(
            response
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