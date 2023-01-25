package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class FakeGetSearchMoviesUseCase {

    val mock: GetSearchMoviesUseCase = mockk()

    fun mockFetchSearchMovies(
        response: MoviesListResult,
    ) {
        coEvery {
            mock.invoke(any())
        } returns flowOf(response)
    }
}
