package com.divinelink.core.datastore

import androidx.compose.ui.graphics.Color
import app.cash.turbine.test
import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.seedLong
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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

    storage.selectedTheme.first() shouldBe "test_theme"
  }

  @Test
  fun `test default selectedTheme is System`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.selectedTheme.first() shouldBe Theme.SYSTEM.storageKey
    storage.selectedTheme.first() shouldNotBe Theme.DARK.storageKey
    storage.selectedTheme.first() shouldNotBe Theme.LIGHT.storageKey
  }

  @Test
  fun `test color system updates`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.colorSystem.test {
      awaitItem() shouldBe ColorSystem.Dynamic
      storage.setColorSystem(ColorSystem.Default)

      awaitItem() shouldBe ColorSystem.Default
    }
  }

  @Test
  fun `test default custom color and updates`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.customColor.test {
      awaitItem() shouldBe seedLong

      storage.setThemeColor(Color.Green.value.toLong())

      awaitItem() shouldBe Color.Green.value.toLong()
    }
  }

  @Test
  fun `test setBlackBackgrounds sets isBlackBackgroundsEnabled`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setBlackBackgrounds(true)

    storage.isBlackBackgroundsEnabled.test {
      awaitItem() shouldBe true
      storage.setBlackBackgrounds(false)
      awaitItem() shouldBe false
    }
  }

  @Test
  fun `test spoilersObfuscation value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.spoilersObfuscation.test {
      awaitItem() shouldBe false

      storage.setSpoilersObfuscation(true)

      awaitItem() shouldBe true
    }
  }

  @Test
  fun `test spoilersObfuscation does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.spoilersObfuscation.test {
      awaitItem() shouldBe false

      storage.setSpoilersObfuscation(false)
      storage.setSpoilersObfuscation(false)

      expectNoEvents()
    }
  }

  @Test
  fun `test MovieRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.movieRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB
    }
  }

  @Test
  fun `test MovieRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.movieRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setMovieRatingSource(RatingSource.IMDB)
      awaitItem() shouldBe RatingSource.IMDB

      storage.setMovieRatingSource(RatingSource.TRAKT)
      awaitItem() shouldBe RatingSource.TRAKT
    }
  }

  @Test
  fun `test MovieRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.movieRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setMovieRatingSource(RatingSource.TMDB)
      storage.setMovieRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }

  @Test
  fun `test TVRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.tvRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB
    }
  }

  @Test
  fun `test TVRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.tvRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setTvRatingSource(RatingSource.IMDB)
      awaitItem() shouldBe RatingSource.IMDB

      storage.setTvRatingSource(RatingSource.TRAKT)
      awaitItem() shouldBe RatingSource.TRAKT
    }
  }

  @Test
  fun `test TVRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.tvRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setTvRatingSource(RatingSource.TMDB)
      storage.setTvRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }

  @Test
  fun `test EpisodesRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.episodesRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB
    }
  }

  @Test
  fun `test EpisodesRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.episodesRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setEpisodesRatingSource(RatingSource.IMDB)
      awaitItem() shouldBe RatingSource.IMDB

      storage.setEpisodesRatingSource(RatingSource.TRAKT)
      awaitItem() shouldBe RatingSource.TRAKT
    }
  }

  @Test
  fun `test EpisodesRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.episodesRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setEpisodesRatingSource(RatingSource.TMDB)
      storage.setEpisodesRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }

  @Test
  fun `test SeasonsRatingSource default value is TMDB`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.seasonsRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB
    }
  }

  @Test
  fun `test SeasonsRatingSource value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.seasonsRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setSeasonsRatingSource(RatingSource.IMDB)
      awaitItem() shouldBe RatingSource.IMDB

      storage.setSeasonsRatingSource(RatingSource.TRAKT)
      awaitItem() shouldBe RatingSource.TRAKT
    }
  }

  @Test
  fun `test SeasonsRatingSource does not trigger new emissions with the same value`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.seasonsRatingSource.test {
      awaitItem() shouldBe RatingSource.TMDB

      storage.setSeasonsRatingSource(RatingSource.TMDB)
      storage.setSeasonsRatingSource(RatingSource.TMDB)

      expectNoEvents()
    }
  }
}
