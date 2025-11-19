package com.divinelink.core.data.di

import com.divinelink.core.data.network.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class IosConnectivityNetworkManager : NetworkMonitor {
  override val isOnline: Flow<Boolean>
    get() = flowOf(true)
}
