package com.divinelink.core.data.media

import app.cash.turbine.test
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.data.media.repository.ProdMediaRepository
import com.divinelink.core.fixtures.model.GenreFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory.toWizard
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.Resource
import com.divinelink.core.network.media.model.GenresListResponse
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.dao.TestMediaDao
import com.divinelink.core.testing.factories.api.media.GenreResponseFactory
import com.divinelink.core.testing.service.TestMediaService
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class ProdMediaRepositoryTest {

  private val movie = MediaItemFactory.FightClub().toWizard {
    withFavorite(true)
  }

  private var mediaDao = TestMediaDao()
  private var mediaService = TestMediaService()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: MediaRepository

  @Before
  fun setUp() {
    repository = ProdMediaRepository(
      dao = mediaDao.mock,
      remote = mediaService.mock,
      dispatcher = testDispatcher,
    )
  }

  @Test
  fun `test fetch favorites`() = runTest {
    val favoriteMovies = flowOf(
      MediaItemFactory.all(),
    )

    mediaDao.mockFetchFavorites(favoriteMovies)

    repository.fetchFavorites().test {
      awaitItem() shouldBe Result.success(MediaItemFactory.all())
      awaitComplete()
    }
  }

  @Test
  fun `correctly check movie is favorite`() = runTest {
    mediaDao.mockCheckIfFavorite(id = 1, result = true, mediaType = MediaType.MOVIE)

    val result = repository.checkIfMediaIsFavorite(1, MediaType.MOVIE)

    result shouldBe Result.success(true)
  }

  @Test
  fun `correctly check movie is not favorite`() = runTest {
    mediaDao.mockCheckIfFavorite(id = 1, result = false, mediaType = MediaType.MOVIE)

    val result = repository.checkIfMediaIsFavorite(1, MediaType.MOVIE)

    result shouldBe Result.success(false)
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

  @Test
  fun `test fetch movie genres with success`() = runTest {
    mediaDao.mockFetchGenres(
      flowOf(
        emptyList(),
        GenreFactory.Movie.all,
      ),
    )
    mediaService.mockFetchMovieGenres(
      Result.success(GenresListResponse(GenreResponseFactory.Movie.all)),
    )

    repository.fetchGenres(MediaType.MOVIE).test {
      awaitItem() shouldBe Resource.Loading(emptyList())
      awaitItem() shouldBe Resource.Success(emptyList())
      awaitItem() shouldBe Resource.Success(GenreFactory.Movie.all)
      awaitComplete()
    }
  }

  @Test
  fun `test fetch tv genres with success`() = runTest {
    mediaDao.mockFetchGenres(
      flowOf(
        emptyList(),
        GenreFactory.Tv.all,
      ),
    )
    mediaService.mockFetchTvGenres(
      Result.success(GenresListResponse(GenreResponseFactory.Tv.all)),
    )

    repository.fetchGenres(MediaType.TV).test {
      awaitItem() shouldBe Resource.Loading(emptyList())
      awaitItem() shouldBe Resource.Success(emptyList())
      awaitItem() shouldBe Resource.Success(GenreFactory.Tv.all)
      awaitComplete()
    }
  }

  @Test
  fun `test fetch tv genres with success and cached data`() = runTest {
    mediaDao.mockFetchGenres(flowOf(GenreFactory.Tv.all))

    repository.fetchGenres(MediaType.TV).test {
      awaitItem() shouldBe Resource.Loading(GenreFactory.Tv.all)
      awaitItem() shouldBe Resource.Success(GenreFactory.Tv.all)
      awaitComplete()
    }

    mediaService.verifyNoInteractions()
  }

  @Test
  fun `test fetch movie genres with failure`() = runTest {
    mediaDao.mockFetchGenres(flowOf(emptyList()))
    mediaService.mockFetchMovieGenres(Result.failure(AppException.Unknown()))

    repository.fetchGenres(MediaType.MOVIE).test {
      awaitItem() shouldBe Resource.Loading(emptyList())
      awaitItem() shouldBe Resource.Error(AppException.Unknown(), emptyList())
      awaitComplete()
    }
  }

  @Test
  fun `test fetch tv genres with failure`() = runTest {
    mediaDao.mockFetchGenres(flowOf(emptyList()))
    mediaService.mockFetchTvGenres(Result.failure(AppException.Unknown()))

    repository.fetchGenres(MediaType.TV).test {
      awaitItem() shouldBe Resource.Loading(emptyList())
      awaitItem() shouldBe Resource.Error(AppException.Unknown(), emptyList())
      awaitComplete()
    }
  }
}
