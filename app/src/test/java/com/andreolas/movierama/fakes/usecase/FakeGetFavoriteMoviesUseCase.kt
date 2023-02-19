package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetFavoriteMoviesUseCase {

    val mock: GetFavoriteMoviesUseCase = mockk()

    fun mockGetFavoriteMovies(
        response: Flow<MoviesListResult>,
    ) {
        coEvery {
            mock.invoke(any())
        } returns response
    }

    fun mockFetchPopularMovies(
        response: MoviesListResult,
    ) {
        coEvery {
            mock.invoke(any())
        } returns flowOf(response)
    }
}
