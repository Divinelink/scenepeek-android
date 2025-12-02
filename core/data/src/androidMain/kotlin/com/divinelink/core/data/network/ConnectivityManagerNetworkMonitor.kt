package com.divinelink.core.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.NetworkRequest.Builder
import android.os.Build
import androidx.core.content.getSystemService
import androidx.tracing.trace
import com.divinelink.core.commons.domain.DispatcherProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

internal class ConnectivityManagerNetworkMonitor(
  private val context: Context,
  dispatcher: DispatcherProvider,
) : NetworkMonitor {
  override val isOnline: Flow<Boolean> = callbackFlow {
    trace("NetworkMonitor.callbackFlow") {
      val connectivityManager = context.getSystemService<ConnectivityManager>()
      if (connectivityManager == null) {
        channel.trySend(false)
        channel.close()
        return@callbackFlow
      }

      /**
       * The callback's methods are invoked on changes to *any* network matching
       * the [NetworkRequest], not just the active network.
       * So we can simply track the presence (or absence) of such [Network].
       */
      val callback = object : NetworkCallback() {
        private val networks = mutableSetOf<Network>()

        override fun onAvailable(network: Network) {
          networks += network
          channel.trySend(true)
        }

        override fun onLost(network: Network) {
          networks -= network
          channel.trySend(networks.isNotEmpty())
        }
      }

      trace("NetworkMonitor.registerNetworkCallback") {
        val request = Builder()
          .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
          .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
          .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
          .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
          .build()
        connectivityManager.registerNetworkCallback(request, callback)
      }

      channel.trySend(connectivityManager.isCurrentlyConnected())

      awaitClose {
        connectivityManager.unregisterNetworkCallback(callback)
      }
    }
  }
    .flowOn(dispatcher.io)
    .conflate()

  @Suppress("DEPRECATION")
  private fun ConnectivityManager.isCurrentlyConnected() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
      hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    else -> activeNetworkInfo?.isConnected == true
  }

  private fun ConnectivityManager.hasCapability(capability: Int): Boolean =
    getNetworkCapabilities(activeNetwork)?.hasCapability(capability) == true
}
