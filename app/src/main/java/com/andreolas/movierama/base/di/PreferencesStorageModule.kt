package com.andreolas.movierama.base.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.andreolas.movierama.base.storage.DataStorePreferenceStorage
import com.andreolas.movierama.base.storage.EncryptedPreferenceStorage
import com.andreolas.movierama.base.storage.EncryptedStorage
import com.andreolas.movierama.base.storage.PreferenceStorage
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
    ): EncryptedStorage =
        EncryptedPreferenceStorage(
            preferenceStorage = preferenceStorage,
            context = context,
        )
}
