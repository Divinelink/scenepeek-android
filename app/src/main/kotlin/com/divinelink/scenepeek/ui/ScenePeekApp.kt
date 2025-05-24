package com.divinelink.scenepeek.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.divinelink.core.designsystem.component.ScenePeekNavigationSuiteScaffold
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.model.network.NetworkState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.network.NetworkStatusIndicator
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import com.divinelink.feature.details.navigation.navigateToDetails
import com.divinelink.feature.details.navigation.navigateToPerson
import com.divinelink.feature.onboarding.navigation.navigateToOnboarding
import com.divinelink.scenepeek.MainUiEvent
import com.divinelink.scenepeek.MainUiState
import com.divinelink.scenepeek.navigation.ScenePeekNavHost
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
  windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
  val snackbarHostState = remember { SnackbarHostState() }
  val isOffline by state.isOffline.collectAsStateWithLifecycle()
  val showOnboarding by state.shouldShowOnboarding.collectAsStateWithLifecycle()
  val showBottomNavigation by state.showBottomNavigation.collectAsStateWithLifecycle()
  var networkState by remember { mutableStateOf<NetworkState>(NetworkState.Online.Persistent) }

  val currentDestination = state.currentDestination

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
    ProvideSnackbarController(
      snackbarHostState = snackbarHostState,
      coroutineScope = state.scope,
    ) {
      ScenePeekNavigationSuiteScaffold(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        showNavigationSuite = !showOnboarding && showBottomNavigation,
//        windowInsets = if (
//          networkState is NetworkState.Offline || networkState is NetworkState.Online.Initial
//        ) {
//          WindowInsets(bottom = MaterialTheme.dimensions.keyline_8)
//        } else {
//          NavigationBarDefaults.windowInsets
//        },
        navigationSuiteItems = {
          state.topLevelDestinations.forEach { destination ->
            val selected = currentDestination.isRouteInHierarchy(destination.route::class)
            item(
              selected = selected,
              onClick = { state.navigateToTopLevelDestination(destination) },
              icon = {
                Icon(
                  imageVector = destination.unselectedIcon,
                  contentDescription = null,
                )
              },
              selectedIcon = {
                Icon(
                  imageVector = destination.selectedIcon,
                  contentDescription = null,
                )
              },
              label = { Text(stringResource(destination.iconTextId)) },
              modifier = Modifier,
            )
          }
        },
        content = { innerPadding ->
          Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
          ) { _ ->
            CompositionLocalProvider(
              LocalBottomNavigationPadding provides innerPadding.calculateBottomPadding(),
            ) {
              when (uiState) {
                is MainUiState.Completed -> ScenePeekNavHost(state = state)
                MainUiState.Loading -> LoadingContent()
              }
            }
          }
        },
      )
    }
    NetworkStatusIndicator(networkState = networkState)
  }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) = this?.hierarchy?.any {
  it.hasRoute(route)
} ?: false
