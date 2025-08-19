package com.divinelink.scenepeek.popular.domain.repository

import app.cash.turbine.test
import com.divinelink.core.commons.domain.data
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory.toWizard
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchResponseApi
import com.divinelink.core.testing.dao.TestMediaDao
import com.divinelink.core.testing.factories.api.movie.MovieApiFactory
import com.divinelink.core.testing.service.TestMediaService
import com.divinelink.factories.api.SearchMovieApiFactory
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

  private var mediaDao = TestMediaDao()
  private var mediaService = TestMediaService()

  private lateinit var repository: MediaRepository

  @Before
  fun setUp() {
    repository = ProdMediaRepository(
      dao = mediaDao.mock,
      remote = mediaService.mock,
    )
  }

  @Test
  fun `test fetch popular movies with no favorite ids`() = runTest {
    val request = MoviesRequestApi(page = 1)
    val expectedResult = MediaItemFactory.MoviesList()

    val expectApiPopularResponse = flowOf(apiPopularResponse)

    mediaDao.mockFetchFavoriteMovieIds(flowOf(emptyList()))

    mediaService.mockFetchPopularMovies(
      request = request,
      result = expectApiPopularResponse,
    )

    repository.fetchPopularMovies(request).test {
      assertThat(awaitItem()).isEqualTo(Result.success(expectedResult))
      awaitComplete()
    }
  }

  @Test
  fun `test fetch popular movies with favorite ids`() = runTest {
    val request = MoviesRequestApi(page = 1)

    val expectApiPopularResponse = flowOf(apiPopularResponse)

    mediaDao.mockFetchFavoriteMovieIds(
      flowOf(
        emptyList(),
        emptyList(),
        listOf(1, 2),
      ),
    )

    mediaService.mockFetchPopularMovies(
      request = request,
      result = expectApiPopularResponse,
    )

    repository.fetchPopularMovies(request).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaItemFactory.MoviesList()),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(
          buildList {
            addAll(MediaItemFactory.MoviesList(1..2).map { it.copy(isFavorite = true) })
            addAll(MediaItemFactory.MoviesList(3..10))
          },
        ),
      )

      awaitComplete()
    }
  }

  @Test
  fun `given no movies are favorite, when searching movies, then I expect non favorite movies`() =
    runTest {
      val request = SearchRequestApi(query = "test123", 3)
      val expectedResult = MediaItemFactory.MoviesList()

      val expectedApiSearchResponse = flowOf(apiSearchResponse)
      expectedResult.forEach { movie ->
        mediaDao.mockCheckIfFavorite(
          id = movie.id,
          mediaType = MediaType.MOVIE,
          result = false,
        )
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
  fun `test fetch favorites`() = runTest {
    val favoriteMovies = flowOf(
      MediaItemFactory.all(),
    )

    mediaDao.mockFetchFavorites(favoriteMovies)

    repository.fetchFavorites().test {
      assertThat(awaitItem()).isEqualTo(Result.success(MediaItemFactory.all()))
      awaitComplete()
    }
  }

  @Test
  fun `correctly check movie is favorite`() = runTest {
    mediaDao.mockCheckIfFavorite(id = 1, result = true, mediaType = MediaType.MOVIE)

    val result = repository.checkIfMediaIsFavorite(1, MediaType.MOVIE)

    assertThat(result).isEqualTo(Result.success(true))
  }

  @Test
  fun `correctly check movie is not favorite`() = runTest {
    mediaDao.mockCheckIfFavorite(id = 1, result = false, mediaType = MediaType.MOVIE)

    val result = repository.checkIfMediaIsFavorite(1, MediaType.MOVIE)

    assertThat(result).isEqualTo(Result.success(false))
  }

  @Test
  fun testInsertMovie() = runTest {
    repository.insertFavoriteMedia(movie)

    mediaDao.verifyInsertFavoriteMovie(movie.id)
  }

  @Test
  fun testRemoveMovie() = runTest {
    repository.removeFavoriteMedia(movie.id, mediaType = MediaType.MOVIE)

    mediaDao.verifyRemoveMovie(movie.id)
  }
}
