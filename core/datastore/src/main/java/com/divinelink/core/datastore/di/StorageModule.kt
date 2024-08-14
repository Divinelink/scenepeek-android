package com.divinelink.core.datastore.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.datastore.EncryptedPreferenceStorage
import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.datastore.SessionStorage
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(DataStorePreferenceStorage.PREFS_NAME)

val storageModule = module {

  single<PreferenceStorage> {
    val context: Context = get()
    DataStorePreferenceStorage(context.dataStore)
  }

  single<EncryptedStorage> {
    val context: Context = get()
    val preferenceStorage: PreferenceStorage = get()
    EncryptedPreferenceStorage(preferenceStorage, context)
  }

  single<SessionStorage> {
    val storage: PreferenceStorage = get()
    val encryptedStorage: EncryptedStorage = get()
    SessionStorage(storage, encryptedStorage)
  }
}
