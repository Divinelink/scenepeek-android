package com.andreolas.movierama

import android.app.Application
import com.andreolas.movierama.base.di.appModule
import com.andreolas.movierama.base.di.appRemoteModule
import com.andreolas.movierama.base.di.appRepositoryModule
import com.andreolas.movierama.base.di.appUseCaseModule
import com.andreolas.movierama.base.di.appViewModelModule
import com.divinelink.core.commons.di.commonModule
import com.divinelink.core.commons.di.dispatcherModule
import com.divinelink.core.data.di.dataModule
import com.divinelink.core.database.di.databaseModule
import com.divinelink.core.datastore.di.storageModule
import com.divinelink.core.domain.di.useCaseModule
import com.divinelink.core.network.di.remoteModule
import com.divinelink.feature.settings.di.settingsUseCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup.onKoinStartup

class Application : Application() {

  init {
    onKoinStartup {
      androidContext(this@Application)
      modules(
        appModule,
        appRemoteModule,
        appRepositoryModule,
        appViewModelModule,
        appUseCaseModule,
        commonModule,
        dataModule,
        databaseModule,
        dispatcherModule,
        remoteModule,
        useCaseModule,
        storageModule,
        settingsUseCaseModule,
      )
    }
  }
}
