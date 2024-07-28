package com.andreolas.movierama.base.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.divinelink.core.commons.di.ApplicationScope
import com.divinelink.core.commons.di.DefaultDispatcher
import com.divinelink.core.database.AppDatabase
import com.divinelink.core.database.AppDatabase.Companion.DB_NAME
import com.divinelink.core.network.client.RestClient
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationComponent
  // (i.e. everywhere in the application)
  @Provides
  fun provideYourDatabase(
    @ApplicationContext app: Context,
  ) = Room.databaseBuilder(
    context = app,
    klass = AppDatabase::class.java,
    name = DB_NAME,
  ).fallbackToDestructiveMigration().build() // The reason we can construct a database for the repo

  @Singleton
  @Provides
  fun provideMovieDAO(database: AppDatabase) = database.mediaDao()

  @ApplicationContext
  @Provides
  fun providesApplicationContext() = Application()

  @Singleton
  @Provides
  fun provideRestClient(engine: HttpClientEngine): RestClient = RestClient(engine)

  @Singleton
  @Provides
  fun provideFirebaseRemoteConfig(
    configSettings: FirebaseRemoteConfigSettings,
  ): FirebaseRemoteConfig = Firebase.remoteConfig.apply {
    setConfigSettingsAsync(configSettings)
  }

  @Singleton
  @Provides
  fun provideFirebaseRemoteConfigSettings(): FirebaseRemoteConfigSettings = if (BuildConfig.DEBUG) {
    remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
  } else {
    remoteConfigSettings { }
  }

  @ApplicationScope
  @Singleton
  @Provides
  fun providesApplicationScope(
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
  ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)
}
