package com.divinelink.core.datastore

import app.cash.turbine.test
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import kotlin.test.AfterTest
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class DataStorePreferenceStorageTest {

  private lateinit var storage: DataStorePreferenceStorage

  private val fakeDataStore = TestDatastoreFactory.create()

  @AfterTest
  fun tearDown() {
    stopKoin()
  }

  @Test
  fun `test selectTheme sets selectedTheme`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.selectTheme("test_theme")

    assertThat(storage.selectedTheme.first()).isEqualTo("test_theme")
  }

  @Test
  fun `test default selectedTheme is System`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    assertThat(storage.selectedTheme.first()).isEqualTo(Theme.SYSTEM.storageKey)
    assertThat(storage.selectedTheme.first()).isNotEqualTo(Theme.DARK.storageKey)
    assertThat(storage.selectedTheme.first()).isNotEqualTo(Theme.LIGHT.storageKey)
  }

  @Test
  fun `test setMaterialYou sets isMaterialYouEnabled`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)
    storage.setMaterialYou(true)

    assertThat(storage.isMaterialYouEnabled.first()).isTrue()
  }

  @Test
  fun `test setBlackBackgrounds sets isBlackBackgroundsEnabled`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setBlackBackgrounds(true)

    assertThat(storage.isBlackBackgroundsEnabled.first()).isTrue()

    storage.setBlackBackgrounds(false)

    assertThat(storage.isBlackBackgroundsEnabled.first()).isFalse()
  }

  @Test
  fun `test spoilersObfuscation value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.spoilersObfuscation.test {
      assertThat(awaitItem()).isEqualTo(false)

      storage.setSpoilersObfuscation(true)

      assertThat(awaitItem()).isEqualTo(true)
    }
  }

  @Test
  fun `test spoilersObfuscation does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.spoilersObfuscation.test {
      assertThat(awaitItem()).isEqualTo(false)

      storage.setSpoilersObfuscation(false)
      storage.setSpoilersObfuscation(false)

      expectNoEvents()
    }
  }

  @Test
  fun `test MovieRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.movieRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)
    }
  }

  @Test
  fun `test MovieRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.movieRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setMovieRatingSource(RatingSource.IMDB)
      assertThat(awaitItem()).isEqualTo(RatingSource.IMDB)

      storage.setMovieRatingSource(RatingSource.TRAKT)
      assertThat(awaitItem()).isEqualTo(RatingSource.TRAKT)
    }
  }

  @Test
  fun `test MovieRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.movieRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setMovieRatingSource(RatingSource.TMDB)
      storage.setMovieRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }

  @Test
  fun `test TVRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.tvRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)
    }
  }

  @Test
  fun `test TVRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.tvRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setTvRatingSource(RatingSource.IMDB)
      assertThat(awaitItem()).isEqualTo(RatingSource.IMDB)

      storage.setTvRatingSource(RatingSource.TRAKT)
      assertThat(awaitItem()).isEqualTo(RatingSource.TRAKT)
    }
  }

  @Test
  fun `test TVRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.tvRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setTvRatingSource(RatingSource.TMDB)
      storage.setTvRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }

  @Test
  fun `test EpisodesRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.episodesRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)
    }
  }

  @Test
  fun `test EpisodesRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.episodesRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setEpisodesRatingSource(RatingSource.IMDB)
      assertThat(awaitItem()).isEqualTo(RatingSource.IMDB)

      storage.setEpisodesRatingSource(RatingSource.TRAKT)
      assertThat(awaitItem()).isEqualTo(RatingSource.TRAKT)
    }
  }

  @Test
  fun `test EpisodesRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.episodesRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setEpisodesRatingSource(RatingSource.TMDB)
      storage.setEpisodesRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }

  @Test
  fun `test SeasonsRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.seasonsRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)
    }
  }

  @Test
  fun `test SeasonsRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.seasonsRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setSeasonsRatingSource(RatingSource.IMDB)
      assertThat(awaitItem()).isEqualTo(RatingSource.IMDB)

      storage.setSeasonsRatingSource(RatingSource.TRAKT)
      assertThat(awaitItem()).isEqualTo(RatingSource.TRAKT)
    }
  }

  @Test
  fun `test SeasonsRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.seasonsRatingSource.test {
      assertThat(awaitItem()).isEqualTo(RatingSource.TMDB)

      storage.setSeasonsRatingSource(RatingSource.TMDB)
      storage.setSeasonsRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }
}
