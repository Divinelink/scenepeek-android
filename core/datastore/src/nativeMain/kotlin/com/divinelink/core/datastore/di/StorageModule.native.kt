package com.divinelink.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.datastore.auth.DataStoreSavedStateStorage
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.crypto.DataEncryptor
import com.divinelink.core.datastore.crypto.DataStoreKeystoreSecretsStorage
import com.divinelink.core.datastore.crypto.EncryptionProvider
import com.divinelink.core.datastore.crypto.KeystoreSecretsStorage
import com.divinelink.core.datastore.onboarding.DataStoreOnboardingStorage
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import com.divinelink.core.datastore.ui.DatastoreUiStorage
import com.divinelink.core.datastore.ui.UiSettingsStorage
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

fun createDataStore(producePath: () -> String): DataStore<Preferences> = PreferenceDataStoreFactory
  .createWithPath(produceFile = { producePath().toPath() })

@OptIn(ExperimentalForeignApi::class)
internal fun createDataStore(name: String): DataStore<Preferences> = createDataStore(
  producePath = {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
      directory = NSDocumentDirectory,
      inDomain = NSUserDomainMask,
      appropriateForURL = null,
      create = false,
      error = null,
    )
    // Ensure the name has the required .preferences_pb extension
    val fileName = if (name.endsWith(".preferences_pb")) name else "$name.preferences_pb"
    requireNotNull(documentDirectory).path + "/$fileName"
  },
)

actual val storageModule = module {

  single<PreferenceStorage> {
    DataStorePreferenceStorage(
      dataStore = createDataStore(DataStorePreferenceStorage.PREFS_NAME),
    )
  }

  single<OnboardingStorage> {
    DataStoreOnboardingStorage(
      dataStore = createDataStore(DataStoreOnboardingStorage.PREFS_NAME),
    )
  }

  single<UiSettingsStorage> {
    DatastoreUiStorage(
      dataStore = createDataStore(DatastoreUiStorage.PREFS_NAME),
    )
  }

  single<SavedStateStorage> {
    DataStoreSavedStateStorage(
      dataStore = createDataStore(DataStoreSavedStateStorage.NAME),
      json = get(),
      scope = get(),
      encryptor = get(),
    )
  }

  single<KeystoreSecretsStorage> {
    DataStoreKeystoreSecretsStorage(
      dataStore = createDataStore(DataStoreKeystoreSecretsStorage.PREFS_NAME),
    )
  }

  // TODO KMP
  /**
   *   single<EncryptedStorage> {
   *     val preferenceStorage = try {
   *       getEncryptedSharedPreferences(application = get())
   *     } catch (_: GeneralSecurityException) {
   *       // Handle when a bad master key or key-set has been attempted
   *       destroyEncryptedSharedPreferencesAndRebuild(application = get())
   *     } catch (@Suppress("TooGenericExceptionCaught") _: RuntimeException) {
   *       // Handle KeystoreExceptions that get wrapped up in a RuntimeException
   *       destroyEncryptedSharedPreferencesAndRebuild(application = get())
   *     }
   *
   *     EncryptedPreferenceStorage(encryptedPreferences = preferenceStorage)
   *   }
   */

  singleOf(::EncryptionProvider) { bind<DataEncryptor>() }

  singleOf(::SessionStorage)
}
