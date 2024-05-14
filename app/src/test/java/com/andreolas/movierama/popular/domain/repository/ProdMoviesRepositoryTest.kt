package com.andreolas.movierama.popular.domain.repository

import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.MediaItemFactory.toWizard
import com.andreolas.factories.api.PopularMovieApiFactory
import com.andreolas.factories.api.SearchMovieApiFactory
import com.andreolas.movierama.fakes.dao.FakeMediaDao
import com.andreolas.movierama.fakes.remote.FakeMediaService
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.popular.PopularRequestApi
import com.divinelink.core.network.media.model.popular.PopularResponseApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchResponseApi
import com.divinelink.database.model.PersistableMovie
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.data
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProdMoviesRepositoryTest {

  private val movie = MediaItemFactory.FightClub().toWizard {
    withFavorite(true)
  }

  private val persistableMovie = com.divinelink.database.model.PersistableMovie(
    id = 1123,
    posterPath = "123456",
    releaseDate = "2022",
    title = "Flight Club",
    rating = "7.3",
    overview = "This movie is good.",
    isFavorite = true,
  )

  private val apiPopularResponse = PopularResponseApi(
    page = 1,
    results = PopularMovieApiFactory.EmptyList(),
    totalPages = 0,
    totalResults = 0
  )

  private val apiSearchResponse = SearchResponseApi(
    page = 1,
    results = SearchMovieApiFactory.EmptyList(),
    totalPages = 0,
    totalResults = 0
  )

  private var mediaDao = FakeMediaDao()
  private var mediaRemote = FakeMediaService()

  private lateinit var repository: MediaRepository

  @Before
  fun setUp() {
    repository = ProdMediaRepository(
      mediaDao = mediaDao.mock,
      mediaRemote = mediaRemote.mock,
    )
  }

  @Test
  fun testFetchPopularMovies() = runTest {
    val request = PopularRequestApi(page = 1)
    val expectedResult = MediaItemFactory.MoviesList()

    val expectApiPopularResponse = flowOf(apiPopularResponse)

    expectedResult.forEach { movie ->
      mediaDao.mockCheckIfFavorite(movie.id, 0)
    }

    mediaRemote.mockFetchPopularMovies(
      request = request,
      result = expectApiPopularResponse
    )

    val actualResult = repository.fetchPopularMovies(request).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `given all movies are not favorite, when fetching movies from search api, then I expect non favorite movies`() =
    runTest {
      val request = SearchRequestApi(query = "test123", 3)
      val expectedResult = MediaItemFactory.MoviesList()

      val expectedApiSearchResponse = flowOf(apiSearchResponse)
      expectedResult.forEach { movie ->
        mediaDao.mockCheckIfFavorite(movie.id, 0)
      }
      mediaRemote.mockFetchSearchMovies(
        request = request,
        result = expectedApiSearchResponse
      )

      val actualResult = repository.fetchSearchMovies(
        request = request,
      ).first()

      assertThat(expectedResult).isEqualTo(actualResult.data)
    }

  //    @Test
  //    fun testFetchPopularMoviesErrorCase() = runTest {
  //        val request = PopularRequestApi(apiKey = "", page = 1)
  //        val expectedResult = Result.failure(Exception("response is empty"))
  //
  //        mediaRemote.mockFetchPopularMovies(
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
      movie.copy(id = 1234, name = "Movie Title 2"),
    )

    val expectedPersistableMovieResult = flowOf(
      listOf(
        persistableMovie,
        persistableMovie.copy(id = 1234, title = "Movie Title 2"),
      )
    )

    mediaDao.mockFetchFavoritesMovies(expectedPersistableMovieResult)

    val actualResult = repository.fetchFavoriteMovies().first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `correctly check movie is favorite`() = runTest {
    mediaDao.mockCheckIfFavorite(id = 1, result = 1)

    val result = repository.checkIfMediaIsFavorite(1, MediaType.MOVIE)

    assertThat(result).isEqualTo(Result.success(true))
  }

  @Test
  fun `correctly check movie is not favorite`() = runTest {
    mediaDao.mockCheckIfFavorite(id = 1, result = 0)

    val result = repository.checkIfMediaIsFavorite(1, MediaType.MOVIE)

    assertThat(result).isEqualTo(Result.success(false))
  }

  @Test
  fun testInsertMovie() = runTest {
    repository.insertFavoriteMedia(movie)

    mediaDao.verifyInsertFavoriteMovie(persistableMovie)
  }

  @Test
  fun testRemoveMovie() = runTest {
    repository.removeFavoriteMedia(movie.id, mediaType = MediaType.MOVIE)

    mediaDao.verifyRemoveMovie(persistableMovie.id)
  }
}
