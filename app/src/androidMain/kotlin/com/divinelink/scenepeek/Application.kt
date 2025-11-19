package com.divinelink.scenepeek

import android.app.Application
import com.divinelink.scenepeek.shared.configKoinModules
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
    configKoinModules()
  }
}
