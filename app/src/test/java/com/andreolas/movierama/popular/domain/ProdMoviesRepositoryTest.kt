package com.andreolas.movierama.popular.domain

import com.andreolas.movierama.base.data.local.popular.PersistableMovie
import com.andreolas.movierama.fakes.dao.FakeMovieDAO
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

    val persistableMovie = PersistableMovie(
        id = 1123,
        posterPath = "123456asdfghjmnbvcx",
        releaseDate = "2022",
        title = "Flight Club",
        rating = "99",
        isFavorite = true,
    )

    private var movieDAO = FakeMovieDAO()

    private lateinit var repository: MoviesRepository

    @Before
    fun setUp() {
        repository = ProdMoviesRepository(movieDAO.mock)
    }

    @Test
    fun testFetchAllMovies() = runTest {
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
