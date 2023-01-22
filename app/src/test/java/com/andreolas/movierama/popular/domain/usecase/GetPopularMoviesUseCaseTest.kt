package com.andreolas.movierama.popular.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPopularMoviesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var repository: FakeMoviesRepository

    private val request = PopularRequestApi(apiKey = "", page = 0)

    private val localFavoriteMovies = (1..3).map { index ->
        PopularMovie(
            id = index.toLong(),
            posterPath = "",
            releaseDate = "2000",
            title = "Fight Club $index",
            isFavorite = true,
            rating = index.toString(),
        )
    }.toMutableList()

    private val remoteMovies = (1..6).map { index ->
        PopularMovie(
            id = index.toLong(),
            posterPath = "",
            releaseDate = "2000",
            title = "Fight Club $index",
            isFavorite = false,
            rating = index.toString(),
        )
    }.toMutableList()

    private val mergedListResult = (1..3).map { index ->
        PopularMovie(
            id = index.toLong(),
            posterPath = "",
            releaseDate = "2000",
            title = "Fight Club $index",
            isFavorite = true,
            rating = index.toString(),
        )
    }.toMutableList().also { list ->
        list.addAll(
            (4..6).map { index ->
                PopularMovie(
                    id = index.toLong(),
                    posterPath = "",
                    releaseDate = "2000",
                    title = "Fight Club $index",
                    isFavorite = false,
                    rating = index.toString(),
                )
            }.toMutableList()
        )
    }

    @Before
    fun setUp() {
        repository = FakeMoviesRepository()
    }

    @Test
    fun `given 3 favorite movies when I fetch Popular movies then I expect success data`() = runTest {
        val expectedResult = Result.Success<List<PopularMovie>>(mergedListResult)

        repository.mockFetchFavoriteMovies(
            Result.Success(localFavoriteMovies)
        )

        repository.mockFetchPopularMovies(
            request = PopularRequestApi(apiKey = "", page = 0),
            response = Result.Success(remoteMovies)
        )

        val useCase = GetPopularMoviesUseCase(
            moviesRepository = repository.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).first()

        assertThat(result).isEqualTo(expectedResult)
    }
}
