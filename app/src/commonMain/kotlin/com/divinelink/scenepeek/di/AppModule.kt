package com.divinelink.scenepeek.di

import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.commons.provider.getBuildConfigProvider
import com.divinelink.scenepeek.ui.ThemedActivityDelegate
import com.divinelink.scenepeek.ui.ThemedActivityDelegateImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

  single<CoroutineScope> { CoroutineScope(SupervisorJob()) }

  single<BuildConfigProvider> { getBuildConfigProvider() }

  singleOf(::ThemedActivityDelegateImpl) { bind<ThemedActivityDelegate>() }
}
