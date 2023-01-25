package com.andreolas.movierama.fakes.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeDetailsRepository {

    val mock: DetailsRepository = mock()

    fun mockFetchFavoriteMovies(
        request: DetailsRequestApi,
        response: Result<MovieDetails>,
    ) {
        whenever(
            mock.fetchMovieDetails(request)
        ).thenReturn(
            flowOf(response)
        )
    }
}
