package com.divinelink.scenepeek.di

import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.commons.provider.getBuildConfigProvider
import com.divinelink.scenepeek.shared.LocaleManager
import com.divinelink.scenepeek.shared.getLocalManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val appModule = module {

  single<CoroutineScope> { CoroutineScope(SupervisorJob()) }

  single<BuildConfigProvider> { getBuildConfigProvider() }

  single<LocaleManager> { getLocalManager() }
}
