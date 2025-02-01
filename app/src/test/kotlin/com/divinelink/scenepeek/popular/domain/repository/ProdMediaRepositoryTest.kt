package com.divinelink.scenepeek.popular.domain.repository

import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.database.media.model.PersistableMovie
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory.toWizard
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchResponseApi
import com.divinelink.core.testing.factories.api.movie.MovieApiFactory
import com.divinelink.core.testing.service.TestMediaService
import com.divinelink.factories.api.SearchMovieApiFactory
import com.divinelink.scenepeek.fakes.dao.FakeMediaDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

// TODO Restructure to data unit test package
class ProdMediaRepositoryTest {

  private val movie = MediaItemFactory.FightClub().toWizard {
    withFavorite(true)
  }

  private val persistableMovie = PersistableMovie(
    id = 1123,
    posterPath = "123456",
    releaseDate = "2022",
    title = "Flight Club",
    voteAverage = 7.3,
    voteCount = 123_456,
    overview = "This movie is good.",
    isFavorite = true,
  )

  private val apiPopularResponse = MoviesResponseApi(
    page = 1,
    results = MovieApiFactory.EmptyList(),
    totalPages = 0,
    totalResults = 0,
  )

  private val apiSearchResponse = SearchResponseApi(
    page = 1,
    results = SearchMovieApiFactory.EmptyList(),
    totalPages = 0,
    totalResults = 0,
  )

  private var mediaDao = FakeMediaDao()
  private var mediaService = TestMediaService()

  private lateinit var repository: MediaRepository

  @Before
  fun setUp() {
    repository = ProdMediaRepository(
      mediaDao = mediaDao.mock,
      mediaRemote = mediaService.mock,
    )
  }

  @Test
  fun testFetchPopularMovies() = runTest {
    val request = MoviesRequestApi(page = 1)
    val expectedResult = MediaItemFactory.MoviesList()

    val expectApiPopularResponse = flowOf(apiPopularResponse)

    expectedResult.forEach { movie ->
      mediaDao.mockCheckIfFavorite(movie.id, 0)
    }

    mediaService.mockFetchPopularMovies(
      request = request,
      result = expectApiPopularResponse,
    )

    val actualResult = repository.fetchPopularMovies(request).first()

    assertThat(expectedResult).isEqualTo(actualResult.data)
  }

  @Test
  fun `given no movies are favorite, when searching movies, then I expect non favorite movies`() =
    runTest {
      val request = SearchRequestApi(query = "test123", 3)
      val expectedResult = MediaItemFactory.MoviesList()

      val expectedApiSearchResponse = flowOf(apiSearchResponse)
      expectedResult.forEach { movie ->
        mediaDao.mockCheckIfFavorite(movie.id, 0)
      }
      mediaService.mockFetchSearchMovies(
        request = request,
        result = expectedApiSearchResponse,
      )

      val actualResult = repository.fetchSearchMovies(
        request = request,
      ).first()

      assertThat(expectedResult).isEqualTo(actualResult.data)
    }

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
      ),
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
