package com.divinelink.core.datastore.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.datastore.auth.DataStoreSavedStateStorage
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.crypto.AndroidDataEncryptor
import com.divinelink.core.datastore.crypto.DataEncryptor
import com.divinelink.core.datastore.crypto.DataStoreKeystoreSecretsStorage
import com.divinelink.core.datastore.crypto.KeystoreSecretsStorage
import com.divinelink.core.datastore.onboarding.DataStoreOnboardingStorage
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import com.divinelink.core.datastore.ui.DatastoreUiStorage
import com.divinelink.core.datastore.ui.UiSettingsStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(DataStorePreferenceStorage.PREFS_NAME)
private val Context.uiSettingsDataStore by preferencesDataStore(DatastoreUiStorage.PREFS_NAME)
private val Context.onboardingDataStore by preferencesDataStore(
  DataStoreOnboardingStorage.PREFS_NAME,
)
private val Context.savedStateDataStore by preferencesDataStore(DataStoreSavedStateStorage.NAME)
private val Context.keystoreDataStore by preferencesDataStore(
  DataStoreKeystoreSecretsStorage.PREFS_NAME,
)

val storageModule = module {

  single<PreferenceStorage> {
    val context: Context = get()
    DataStorePreferenceStorage(context.dataStore)
  }

  single<OnboardingStorage> {
    val context: Context = get()
    DataStoreOnboardingStorage(context.onboardingDataStore)
  }

  single<UiSettingsStorage> {
    val context: Context = get()
    DatastoreUiStorage(context.uiSettingsDataStore)
  }

  single<SavedStateStorage> {
    val context: Context = get()
    DataStoreSavedStateStorage(
      dataStore = context.savedStateDataStore,
      json = get(),
      scope = get(),
      encryptor = get(),
    )
  }

  single<KeystoreSecretsStorage> {
    val context: Context = get()
    DataStoreKeystoreSecretsStorage(
      dataStore = context.keystoreDataStore,
    )
  }

  singleOf(::AndroidDataEncryptor) { bind<DataEncryptor>() }

  singleOf(::SessionStorage)
}
