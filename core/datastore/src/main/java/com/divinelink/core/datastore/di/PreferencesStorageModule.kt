package com.divinelink.core.datastore.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.divinelink.core.datastore.DataStorePreferenceStorage
import com.divinelink.core.datastore.EncryptedPreferenceStorage
import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesStorageModule {
  private val Context.dataStore by preferencesDataStore(DataStorePreferenceStorage.PREFS_NAME)

  @Singleton
  @Provides
  fun providePreferenceStorage(@ApplicationContext context: Context): PreferenceStorage =
    DataStorePreferenceStorage(context.dataStore)

  @Singleton
  @Provides
  fun provideEncryptedPreferenceStorage(
    preferenceStorage: PreferenceStorage,
    @ApplicationContext context: Context,
  ): EncryptedStorage = EncryptedPreferenceStorage(
    preferenceStorage = preferenceStorage,
    context = context,
  )
}
