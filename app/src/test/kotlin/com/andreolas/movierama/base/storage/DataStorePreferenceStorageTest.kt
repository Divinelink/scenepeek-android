package com.andreolas.movierama.base.storage

import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.testing.datastore.TestDatastoreFactory
import com.divinelink.core.testing.factories.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class DataStorePreferenceStorageTest {

  private lateinit var storage: DataStorePreferenceStorage

  private val fakeDataStore = TestDatastoreFactory.create()

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
  fun `test testJellyseerrSignInMethod`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)
    assertThat(storage.jellyseerrSignInMethod.first()).isEqualTo(null)

    storage.setJellyseerrSignInMethod(JellyseerrLoginMethod.JELLYSEERR.name)
    assertThat(
      storage.jellyseerrSignInMethod.first(),
    ).isEqualTo(JellyseerrLoginMethod.JELLYSEERR.name)
  }

  @Test
  fun `test clearJellyseerrSignInMethod`() = runTest {
    storage = DataStorePreferenceStorage(fakeDataStore)

    storage.setJellyseerrSignInMethod(JellyseerrLoginMethod.JELLYSEERR.name)
    assertThat(
      storage.jellyseerrSignInMethod.first(),
    ).isEqualTo(JellyseerrLoginMethod.JELLYSEERR.name)

    storage.clearJellyseerrSignInMethod()
    assertThat(storage.jellyseerrSignInMethod.first()).isEqualTo(null)
  }
}
