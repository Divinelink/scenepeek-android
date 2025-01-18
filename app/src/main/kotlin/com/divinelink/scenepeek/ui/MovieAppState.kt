package com.divinelink.scenepeek.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.divinelink.core.data.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberMovieAppState(
  networkMonitor: NetworkMonitor,
  scope: CoroutineScope = rememberCoroutineScope(),
): MovieAppState = remember(networkMonitor, scope) {
  MovieAppState(
    scope = scope,
    networkMonitor = networkMonitor,
  )
}

@Stable
class MovieAppState(
  val scope: CoroutineScope,
  networkMonitor: NetworkMonitor,
) {

  val isOffline = networkMonitor.isOnline
    .map(Boolean::not)
    .stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
      initialValue = false,
    )

  companion object {
    val SUBSCRIPTION_TIMEOUT = 5.seconds.inWholeMilliseconds
  }
}
