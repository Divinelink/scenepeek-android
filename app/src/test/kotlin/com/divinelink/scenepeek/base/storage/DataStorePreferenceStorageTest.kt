package com.divinelink.scenepeek.base.storage

import app.cash.turbine.test
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
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
  fun `test setEncryptedPreferences sets encryptedPreferences`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setEncryptedPreferences("test_encrypted_preferences")

    assertThat(storage.encryptedPreferences.first()).isEqualTo("test_encrypted_preferences")
  }

  @Test
  fun `test session`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setHasSession(true)
    storage.hasSession.first().also {
      assertThat(it).isTrue()
    }

    storage.setHasSession(false)
    storage.hasSession.first().also {
      assertThat(it).isFalse()
    }
  }

  @Test
  fun `test setAccountId sets accountId`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    assertThat(storage.accountId.first()).isNull()

    storage.setAccountId("test_account_id")

    assertThat(storage.accountId.first()).isEqualTo("test_account_id")
  }

  @Test
  fun `test clearAccountId removes accountId`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setAccountId("test_account_id")

    assertThat(storage.accountId.first()).isEqualTo("test_account_id")

    storage.clearAccountId()

    assertThat(storage.accountId.first()).isNull()
  }

  @Test
  fun `test setJellyseerrAddress`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    assertThat(storage.jellyseerrAddress.first()).isEqualTo(null)

    storage.setJellyseerrAddress("http://localhost:5050")
    assertThat(storage.jellyseerrAddress.first()).isEqualTo("http://localhost:5050")
  }

  @Test
  fun `test clearJellyseerrAddress`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setJellyseerrAddress("http://localhost:5050")
    assertThat(storage.jellyseerrAddress.first()).isEqualTo("http://localhost:5050")

    storage.clearJellyseerrAddress()
    assertThat(storage.jellyseerrAddress.first()).isEqualTo(null)
  }

  @Test
  fun `test setJellyseerrAccount`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)
    assertThat(storage.jellyseerrAccount.first()).isEqualTo(null)

    val displayName = JellyseerrAccountDetailsFactory.jellyseerr().displayName

    storage.setJellyseerrAccount(displayName)
    assertThat(storage.jellyseerrAccount.first()).isEqualTo(displayName)
  }

  @Test
  fun `test clearJellyseerrAccount`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)
    val displayName = JellyseerrAccountDetailsFactory.jellyseerr().displayName

    storage.setJellyseerrAccount(displayName)
    assertThat(storage.jellyseerrAccount.first()).isEqualTo(displayName)

    storage.clearJellyseerrAccount()
    assertThat(storage.jellyseerrAccount.first()).isEqualTo(null)
  }

  @Test
  fun `test setJellyseerrSignInMethod`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)
    assertThat(storage.jellyseerrAuthMethod.first()).isEqualTo(null)

    storage.setJellyseerrAuthMethod(JellyseerrAuthMethod.JELLYSEERR.name)
    assertThat(
      storage.jellyseerrAuthMethod.first(),
    ).isEqualTo(JellyseerrAuthMethod.JELLYSEERR.name)
  }

  @Test
  fun `test clearJellyseerrSignInMethod`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setJellyseerrAuthMethod(JellyseerrAuthMethod.JELLYSEERR.name)
    assertThat(
      storage.jellyseerrAuthMethod.first(),
    ).isEqualTo(JellyseerrAuthMethod.JELLYSEERR.name)

    storage.clearJellyseerrSignInMethod()
    assertThat(storage.jellyseerrAuthMethod.first()).isEqualTo(null)
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
