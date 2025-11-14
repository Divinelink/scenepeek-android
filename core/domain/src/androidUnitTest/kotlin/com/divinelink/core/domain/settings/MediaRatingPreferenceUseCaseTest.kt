package com.divinelink.core.domain.settings

import app.cash.turbine.test
import com.divinelink.core.model.details.rating.MediaRatingSource
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class MediaRatingPreferenceUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val storage = FakePreferenceStorage()

  @Test
  fun `test collect and update MediaRatingPreference for Movie with TMDB initial`() = runTest {
    storage.setMovieRatingSource(RatingSource.TMDB)
    storage.setTvRatingSource(RatingSource.TMDB)

    val useCase = MediaRatingPreferenceUseCase(storage, testDispatcher)

    useCase(Unit).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Movie to RatingSource.TMDB),
      )
      skipItems(3)

      useCase.setMediaRatingSource(MediaRatingSource.Movie to RatingSource.IMDB)

      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Movie to RatingSource.IMDB),
      )
    }
  }

  @Test
  fun `test collect and update MediaRatingPreference for TV with TMDB initial source`() = runTest {
    storage.setMovieRatingSource(RatingSource.TMDB)
    storage.setTvRatingSource(RatingSource.TMDB)

    val useCase = MediaRatingPreferenceUseCase(storage, testDispatcher)

    useCase(Unit).test {
      skipItems(1)
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.TVShow to RatingSource.TMDB),
      )
      skipItems(2)

      useCase.setMediaRatingSource(MediaRatingSource.TVShow to RatingSource.IMDB)

      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.TVShow to RatingSource.IMDB),
      )
    }
  }

  @Test
  fun `test collect and update MediaRatingPreference for Episodes with TMDB initial`() = runTest {
    storage.setEpisodesRatingSource(RatingSource.TMDB)

    val useCase = MediaRatingPreferenceUseCase(storage, testDispatcher)

    useCase(Unit).test {
      skipItems(2)
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Episodes to RatingSource.TMDB),
      )
      skipItems(1)

      useCase.setMediaRatingSource(MediaRatingSource.Episodes to RatingSource.IMDB)

      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Episodes to RatingSource.IMDB),
      )
    }
  }

  @Test
  fun `test collect and update MediaRatingPreference for Seasons with TMDB initial`() = runTest {
    storage.setSeasonsRatingSource(RatingSource.TMDB)

    val useCase = MediaRatingPreferenceUseCase(storage, testDispatcher)

    useCase(Unit).test {
      skipItems(3)
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Seasons to RatingSource.TMDB),
      )

      useCase.setMediaRatingSource(MediaRatingSource.Seasons to RatingSource.IMDB)

      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Seasons to RatingSource.IMDB),
      )
    }
  }

  @Test
  fun `test collect all sources with various initial sources`() = runTest {
    storage.setMovieRatingSource(RatingSource.IMDB)
    storage.setTvRatingSource(RatingSource.TRAKT)
    storage.setEpisodesRatingSource(RatingSource.TMDB)
    storage.setSeasonsRatingSource(RatingSource.IMDB)

    val useCase = MediaRatingPreferenceUseCase(storage, testDispatcher)

    useCase(Unit).test {
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Movie to RatingSource.IMDB),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.TVShow to RatingSource.TRAKT),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Episodes to RatingSource.TMDB),
      )
      assertThat(awaitItem()).isEqualTo(
        Result.success(MediaRatingSource.Seasons to RatingSource.IMDB),
      )
    }
  }
}
