package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.usecase.GetMovieDetailsUseCase
import gr.divinelink.core.util.domain.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class FakeGetMoviesDetailsUseCase {

    val mock: GetMovieDetailsUseCase = mockk()

    fun mockFetch(
        response: Result<MovieDetailsResult>,
    ) {
        coEvery {
            mock.invoke(any())
        } returns flowOf(response)
    }
}
