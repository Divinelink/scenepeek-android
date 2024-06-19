package com.andreolas.movierama.base.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.platform.app.InstrumentationRegistry
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.testing.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

private const val TEST_DATASTORE_NAME: String = "test_datastore"

@RunWith(RobolectricTestRunner::class)
class DataStorePreferenceStorageTest {

  private lateinit var storage: DataStorePreferenceStorage

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private val testContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

  private val testCoroutineScope = TestScope(testDispatcher)

  private val fakeDataStore: DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
      scope = testCoroutineScope,
      produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
    )

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
  fun `test clearToken removes token`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setToken("test_token")

    assertThat(storage.token.first()).isEqualTo("test_token")

    storage.clearToken()

    val token = storage.token.first()
    assertThat(token).isEqualTo(null)
  }

  @Test
  fun `test setToken sets token`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setToken("test_token")

    assertThat(storage.token.first()).isEqualTo("test_token")
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
}
