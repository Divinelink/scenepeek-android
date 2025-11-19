package com.divinelink.scenepeek.shared

import com.divinelink.core.commons.di.commonModule
import com.divinelink.core.commons.di.dispatcherModule
import com.divinelink.core.data.di.dataModule
import com.divinelink.core.data.di.networkMonitorModule
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
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun setup(isDebug: Boolean) {
  initKoin()

  if (isDebug) {
    setupLogging()
  }
}

fun KoinApplication.configKoinModules() {
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
    networkMonitorModule,
  )
}

private fun initKoin(config: KoinAppDeclaration? = null) {
  startKoin {
    config?.invoke(this)
    configKoinModules()
  }
}

private fun setupLogging() {
  Napier.base(DebugAntilog())
}
