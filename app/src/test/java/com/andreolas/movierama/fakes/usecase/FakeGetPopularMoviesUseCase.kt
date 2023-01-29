package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.repository.MoviesListResult
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetPopularMoviesUseCase {

    /**
     * For reasons unknown for now, Mockito wasn't working when I had to emit flows using a mock.
     * I tried many things. Using mockk seems to work for now. I'll have to get back on that.
     * I guess it's a good thing I'm using a Mock Wrapper so that I can replace them quickly in the future!
     * :)
     */
    val mock: GetPopularMoviesUseCase = mockk()

    fun mockFetchPopularMovies(
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
