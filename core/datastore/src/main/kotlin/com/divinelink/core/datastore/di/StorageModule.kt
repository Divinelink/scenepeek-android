package com.divinelink.core.datastore.di

import android.content.Context
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.datastore.EncryptedPreferenceStorage
import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.SessionStorage
import com.divinelink.core.datastore.account.AccountDetailsSerializer
import com.divinelink.core.datastore.account.AccountPreferenceStorage
import com.divinelink.core.datastore.account.AccountStorage
import com.divinelink.core.datastore.onboarding.DataStoreOnboardingStorage
import com.divinelink.core.datastore.onboarding.OnboardingStorage
import com.divinelink.core.datastore.ui.DatastoreUiStorage
import com.divinelink.core.datastore.ui.UiSettingsStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val Context.accountDetailsDataStore by dataStore(
  fileName = AccountPreferenceStorage.PREFS_NAME,
  serializer = AccountDetailsSerializer,
)

private val Context.dataStore by preferencesDataStore(DataStorePreferenceStorage.PREFS_NAME)
private val Context.uiSettingsDataStore by preferencesDataStore(DatastoreUiStorage.PREFS_NAME)
private val Context.onboardingDataStore by preferencesDataStore(
  DataStoreOnboardingStorage.PREFS_NAME,
)

val storageModule = module {

  single<AccountStorage> {
    val context: Context = get()
    AccountPreferenceStorage(context.accountDetailsDataStore)
  }

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

  singleOf(::EncryptedPreferenceStorage) { bind<EncryptedStorage>() }

  singleOf(::SessionStorage)
}
