package com.divinelink.core.data.di

import com.divinelink.core.data.network.NetworkMonitor
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val networkMonitorModule = module {
  singleOf(::IosConnectivityNetworkManager) { bind<NetworkMonitor>() }
}
