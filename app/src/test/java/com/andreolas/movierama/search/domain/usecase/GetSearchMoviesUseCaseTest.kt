package com.andreolas.movierama.search.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSearchMoviesUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var repository: FakeMoviesRepository

    private val request = SearchRequestApi(query = "test query", page = 1)
    private val searchResult = (1..6).map { index ->
        PopularMovie(
            id = index,
            posterPath = "",
            releaseDate = "2000",
            title = "Fight Club $index",
            isFavorite = false,
            rating = index.toString(),
        )
    }.toMutableList()

    @Before
    fun setUp() {
        repository = FakeMoviesRepository()
    }

    @Test
    fun `given 3 favorite movies and 3 non favorites when I fetch Popular movies then I expect combined list with favorites`() =
        runTest {
            val expectedResult = Result.Success<List<PopularMovie>>(searchResult)

            repository.mockFetchSearchMovies(
                request = request,
                response = Result.Success(searchResult)
            )

            val useCase = GetSearchMoviesUseCase(
                moviesRepository = repository.mock,
                dispatcher = testDispatcher,
            )
            val result = useCase(request).last()

            assertThat(result).isEqualTo(expectedResult)
        }

    @Test
    fun `given remote data failed then I expect Error Result`() = runTest {
        val expectedResult = Result.Error(Exception("Something went wrong."))

        repository.mockFetchSearchMovies(
            request = request,
            response = Result.Error(Exception()),
        )
        val useCase = GetSearchMoviesUseCase(
            moviesRepository = repository.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).first()
        assertThat(result).isInstanceOf(expectedResult::class.java)
    }
}
