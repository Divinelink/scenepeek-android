package com.divinelink.core.scaffold

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.divinelink.core.model.network.NetworkState
import com.divinelink.core.navigation.route.navigateToDetails
import com.divinelink.core.navigation.route.navigateToOnboarding
import com.divinelink.core.navigation.route.navigateToPerson
import com.divinelink.core.ui.MainUiEvent
import com.divinelink.core.ui.MainUiState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.network.NetworkStatusIndicator
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

private const val NETWORK_STATUS_ANIMATION_DURATION = 4000L

@Composable
fun ScenePeekApp(
  state: ScenePeekAppState,
  uiState: MainUiState,
  uiEvent: MainUiEvent,
  onConsumeEvent: () -> Unit,
) {
  val isOffline by state.isOffline.collectAsStateWithLifecycle()
  val showOnboarding by state.shouldShowOnboarding.collectAsStateWithLifecycle()
  var networkState by remember { mutableStateOf<NetworkState>(NetworkState.Online.Persistent) }

  LaunchedEffect(isOffline) {
    state.scope.launch {
      when {
        isOffline -> {
          networkState = NetworkState.Offline.Initial
          delay(NETWORK_STATUS_ANIMATION_DURATION)
          if (networkState is NetworkState.Offline.Initial) {
            networkState = NetworkState.Offline.Persistent
          }
        }
        else -> {
          if (networkState is NetworkState.Offline) {
            networkState = NetworkState.Online.Initial
            delay(NETWORK_STATUS_ANIMATION_DURATION)
            if (networkState is NetworkState.Online.Initial) {
              networkState = NetworkState.Online.Persistent
            }
          }
        }
      }
    }
  }

  LaunchedEffect(uiEvent) {
    when (uiEvent) {
      is MainUiEvent.NavigateToDetails -> {
        state.navController.navigateToDetails(uiEvent.route)
        onConsumeEvent()
      }
      is MainUiEvent.NavigateToPersonDetails -> {
        state.navController.navigateToPerson(uiEvent.route)
        onConsumeEvent()
      }
      MainUiEvent.None -> {
        // Do nothing
      }
    }
  }

  LaunchedEffect(showOnboarding) {
    if (showOnboarding) {
      state.navController.navigateToOnboarding()
    }
  }

  Column(modifier = Modifier.fillMaxSize()) {
    SharedTransitionLayout(
      modifier = Modifier
        .fillMaxSize()
        .weight(1f),
    ) {
      state.sharedTransitionScope = this@SharedTransitionLayout

      ProvideScenePeekAppState(
        appState = state,
      ) {
        ProvideSnackbarController(
          snackbarHostState = state.snackbarHostState,
          coroutineScope = state.scope,
        ) {
          Surface(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
              is MainUiState.Completed -> ScenePeekNavHost()
              MainUiState.Loading -> LoadingContent()
            }
          }
        }
      }
    }
    NetworkStatusIndicator(networkState = networkState)
  }
}

fun NavDestination?.isRouteInHierarchy(route: KClass<*>) = this?.hierarchy?.any {
  it.hasRoute(route)
} ?: false
