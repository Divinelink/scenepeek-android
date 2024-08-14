package com.divinelink.core.datastore.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.datastore.EncryptedPreferenceStorage
import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.SessionStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(DataStorePreferenceStorage.PREFS_NAME)

val storageModule = module {

  single<PreferenceStorage> {
    val context: Context = get()
    DataStorePreferenceStorage(context.dataStore)
  }

  singleOf(::EncryptedPreferenceStorage) { bind<EncryptedStorage>() }

  singleOf(::SessionStorage)
}
