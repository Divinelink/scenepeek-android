package com.andreolas.movierama.popular.domain.repository

import com.andreolas.movierama.base.data.local.popular.PersistableMovie
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularMovieApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchMovieApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.SearchResponseApi
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

    private val apiPopularResponse = PopularResponseApi(
        page = 1,
        results = listOf(
            PopularMovieApi(
                adult = false,
                backdropPath = "",
                genreIds = listOf(),
                id = 0,
                originalLanguage = "",
                originalTitle = "",
                overview = "",
                popularity = 0.0,
                posterPath = "",
                releaseDate = "",
                title = "",
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            )
        ),
        totalPages = 0,
        totalResults = 0
    )

    private val apiSearchResponse = SearchResponseApi(
        page = 1,
        results = (1..10).map {
            SearchMovieApi(
                adult = false,
                backdropPath = "",
                genreIds = listOf(),
                id = 0,
                originalLanguage = "",
                originalTitle = "",
                overview = "",
                popularity = 0.0,
                posterPath = "",
                releaseDate = "",
                title = "",
                video = false,
                voteAverage = 0.0,
                voteCount = 0
            )
        }.toList(),
        totalPages = 0,
        totalResults = 0
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
        val request = PopularRequestApi(page = 1)
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

        val expectApiPopularResponse = flowOf(apiPopularResponse)

        movieRemote.mockFetchPopularMovies(
            request = request,
            result = expectApiPopularResponse
        )

        val actualResult = repository.fetchPopularMovies(request).first() as Result.Success

        assertThat(expectedResult).isEqualTo(actualResult.data)
    }

    @Test
    fun testFetchSearchMovies() = runTest {
        val request = SearchRequestApi(query = "test123", 3)
        val expectedResult = (1..10).map {
            PopularMovie(
                id = 0,
                posterPath = "",
                releaseDate = "",
                title = "",
                rating = "0.0",
                isFavorite = false
            )
        }.toList()

        val expectedApiSearchResponse = flowOf(apiSearchResponse)

        movieRemote.mockFetchSearchMovies(
            request = request,
            result = expectedApiSearchResponse
        )

        val actualResult = repository.fetchSearchMovies(
            request = request,
        ).first() as Result.Success

        assertThat(expectedResult).isEqualTo(actualResult.data)
    }

    //    @Test
    //    fun testFetchPopularMoviesErrorCase() = runTest {
    //        val request = PopularRequestApi(apiKey = "", page = 1)
    //        val expectedResult = Result.Error(Exception("response is empty"))
    //
    //        movieRemote.mockFetchPopularMovies(
    //            request = request,
    //            result = flowOf(),
    //        )
    //
    //        val actualResult = repository.fetchPopularMovies(request)
    //
    //        assertThat(expectedResult).isInstanceOf(actualResult::class.java)
    //    }

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
