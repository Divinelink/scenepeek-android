package com.andreolas.movierama.fakes.usecase

import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.SearchResult
import gr.divinelink.core.util.domain.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class FakeGetSearchMoviesUseCase {

    val mock: GetSearchMoviesUseCase = mockk()

    fun mockFetchSearchMovies(
        response: Result<SearchResult>,
    ) {
        coEvery {
            mock.invoke(any())
        } returns flowOf(response)
    }
}
