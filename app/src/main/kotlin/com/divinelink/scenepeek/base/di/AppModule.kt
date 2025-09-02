package com.divinelink.scenepeek.base.di

import com.divinelink.core.commons.BuildConfigProvider
import com.divinelink.scenepeek.provider.AndroidBuildConfigProvider
import com.divinelink.scenepeek.ui.ThemedActivityDelegate
import com.divinelink.scenepeek.ui.ThemedActivityDelegateImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

  single<CoroutineScope> { CoroutineScope(SupervisorJob()) }

  singleOf(::ThemedActivityDelegateImpl) { bind<ThemedActivityDelegate>() }

  single<BuildConfigProvider> {
    AndroidBuildConfigProvider()
  }
}
