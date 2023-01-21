package com.andreolas.movierama.popular.domain

import com.andreolas.movierama.base.data.local.popular.PersistableMovie
import com.andreolas.movierama.base.data.network.popular.ApiPopularMovie
import com.andreolas.movierama.base.data.network.popular.ApiPopularResponse
import com.andreolas.movierama.fakes.dao.FakeMovieDAO
import com.andreolas.movierama.fakes.dao.FakeMovieRemote
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.repository.MoviesRepository
import com.andreolas.movierama.home.domain.repository.ProdMoviesRepository
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProdMoviesRepositoryTest {

    private val movie = PopularMovie(
        id = 1123,
        posterPath = "123456asdfghjmnbvcx",
        releaseDate = "2022",
        title = "Flight Club",
        rating = "99",
        isFavorite = true,
    )

    private val persistableMovie = PersistableMovie(
        id = 1123,
        posterPath = "123456asdfghjmnbvcx",
        releaseDate = "2022",
        title = "Flight Club",
        rating = "99",
        isFavorite = true,
    )

    private val apiPopularResponse = ApiPopularResponse(
        page = 1,
        results = listOf(
            ApiPopularMovie(
                adult = false,
                backdrop_path = "",
                genre_ids = listOf(),
                id = 0,
                original_language = "",
                original_title = "",
                overview = "",
                popularity = 0.0,
                poster_path = "",
                release_date = "",
                title = "",
                video = false,
                vote_average = 0.0,
                vote_count = 0
            )
        ),
        total_pages = 0,
        total_results = 0
    )

    private var movieDAO = FakeMovieDAO()
    private var movieRemote = FakeMovieRemote()

    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        repository = ProdMoviesRepository(
            movieDAO = movieDAO.mock,
            movieRemote = movieRemote.mock,
        )
    }

    @Test
    fun testFetchPopularMovies() = runTest {
        val expectedResult = listOf(
            PopularMovie(
                id = 0,
                posterPath = "",
                releaseDate = "",
                title = "",
                rating = "0.0",
                isFavorite = false
            )
        )

        val expectApiPopularResponse = flowOf(
            listOf(apiPopularResponse)
        )

        movieRemote.mockFetchPopularMovies(
            page = 1,
            result = expectApiPopularResponse
        )

        val actualResult = repository.fetchPopularMovies(1).first() as Result.Success

        assertThat(expectedResult).isEqualTo(actualResult.data)
    }

    @Test
    fun testFetchPopularMoviesErrorCase() = runTest {
        val expectedResult = Result.Error(Exception("response is empty"))

        val expectedApiPopularResponse = flowOf(
            emptyList<ApiPopularResponse>()
        )

        movieRemote.mockFetchPopularMovies(
            page = 1,
            result = expectedApiPopularResponse
        )

        val actualResult = repository.fetchPopularMovies(1).first() as Result.Error

        assertThat(expectedResult).isInstanceOf(actualResult::class.java)
    }

    @Test
    fun testFetchFavoriteMovies() = runTest {
        val expectedResult = listOf(
            movie,
            movie.copy(id = 1234, title = "Movie Title 2"),
        )

        val expectedPersistableMovieResult = flowOf(
            listOf(
                persistableMovie,
                persistableMovie.copy(id = 1234, title = "Movie Title 2"),
            )
        )

        movieDAO.mockFetchFavoritesMovies(expectedPersistableMovieResult)

        val actualResult = repository.fetchFavoriteMovies().first() as Result.Success

        assertThat(expectedResult).isEqualTo(actualResult.data)
    }

    @Test
    fun testInsertMovie() = runTest {
        repository.insertFavoriteMovie(movie)

        movieDAO.verifyInsertFavoriteMovie(persistableMovie)
    }

    @Test
    fun testRemoveMovie() = runTest {
        repository.removeFavoriteMovie(movie.id)

        movieDAO.verifyRemoveMovie(persistableMovie.id)
    }
}
