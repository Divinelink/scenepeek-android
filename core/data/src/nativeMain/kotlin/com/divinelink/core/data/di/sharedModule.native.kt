package com.divinelink.core.data.di

import com.divinelink.core.data.app.InstallSourceDetector
import com.divinelink.core.data.network.NetworkMonitor
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val sharedDataModule = module {
  singleOf(::IosConnectivityNetworkManager) { bind<NetworkMonitor>() }
  singleOf(::InstallSourceDetector)
}
