package com.divinelink.scenepeek

import android.app.Application
import com.divinelink.core.commons.di.commonModule
import com.divinelink.core.commons.di.dispatcherModule
import com.divinelink.core.data.di.dataModule
import com.divinelink.core.database.di.databaseModule
import com.divinelink.core.database.di.sqlDriverModule
import com.divinelink.core.datastore.di.storageModule
import com.divinelink.core.domain.di.useCaseModule
import com.divinelink.core.network.di.ktorEngineModule
import com.divinelink.core.network.di.remoteModule
import com.divinelink.feature.onboarding.di.onboardingModule
import com.divinelink.scenepeek.di.appModule
import com.divinelink.scenepeek.di.appRemoteModule
import com.divinelink.scenepeek.di.appRepositoryModule
import com.divinelink.scenepeek.di.appUseCaseModule
import com.divinelink.scenepeek.di.appViewModelModule
import com.divinelink.scenepeek.di.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class Application :
  Application(),
  KoinStartup {

  override fun onKoinStartup() = koinConfiguration {
    androidContext(this@Application)
    modules(
      appModule,
      appRemoteModule,
      appRepositoryModule,
      appViewModelModule,
      appUseCaseModule,
      commonModule,
      dataModule,
      sqlDriverModule,
      databaseModule,
      dispatcherModule,
      onboardingModule,
      ktorEngineModule,
      remoteModule,
      useCaseModule,
      storageModule,
      navigationModule,
    )
  }
}
