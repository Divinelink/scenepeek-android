package com.andreolas.movierama.base.di

import androidx.room.Room
import com.andreolas.movierama.ui.ThemedActivityDelegate
import com.andreolas.movierama.ui.ThemedActivityDelegateImpl
import com.divinelink.core.database.AppDatabase
import com.divinelink.core.database.AppDatabase.Companion.DB_NAME
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.network.client.RestClient
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val appModule = module {

  single {
    Room.databaseBuilder(
      context = get(),
      klass = AppDatabase::class.java,
      name = DB_NAME,
    ).fallbackToDestructiveMigration().build()
  }

  single<MediaDao> { get<AppDatabase>().mediaDao() }

  // Transfer to network module
  single<RestClient> { RestClient(get<HttpClientEngine>()) }

  single {
    Firebase.remoteConfig.apply {
      setConfigSettingsAsync(get())
    }
  }

  single {
    if (BuildConfig.DEBUG) {
      FirebaseRemoteConfigSettings.Builder()
        .setMinimumFetchIntervalInSeconds(0)
        .build()
    } else {
      FirebaseRemoteConfigSettings.Builder().build()
    }
  }

  single<CoroutineScope> { CoroutineScope(SupervisorJob()) }

  single<ThemedActivityDelegate> {
    ThemedActivityDelegateImpl(
      externalScope = get(),
      observeThemeUseCase = get(),
      getThemeUseCase = get(),
      observeMaterialYouUseCase = get(),
      observeBlackBackgroundsUseCase = get(),
    )
  }
}
