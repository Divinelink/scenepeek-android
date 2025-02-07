package com.divinelink.scenepeek.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.network.NetworkState
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.network.NetworkStatusIndicator
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController
import com.divinelink.feature.details.navigation.navigateToDetails
import com.divinelink.feature.details.navigation.navigateToPerson
import com.divinelink.scenepeek.MainUiEvent
import com.divinelink.scenepeek.MainUiState
import com.divinelink.scenepeek.R
import com.divinelink.scenepeek.navigation.ScenePeekNavHost
import com.divinelink.scenepeek.navigation.TopLevelDestination
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
  val snackbarHostState = remember { SnackbarHostState() }
  val isOffline by state.isOffline.collectAsStateWithLifecycle()
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

  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = state.scope,
  ) {
    Scaffold(
      contentWindowInsets = WindowInsets(0, 0, 0, 0),
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      bottomBar = {
        Column {
          AppNavigationBar(
            windowInsets = if (
              networkState is NetworkState.Offline || networkState is NetworkState.Online.Initial
            ) {
              WindowInsets(bottom = MaterialTheme.dimensions.keyline_8)
            } else {
              NavigationBarDefaults.windowInsets
            },
          ) {
            TopLevelDestination.entries.forEach { destination ->
              val selected = state.currentDestination.isRouteInHierarchy(destination.route::class)

              NavigationBarItem(
                selected = selected,
                onClick = { state.navigateToTopLevelDestination(destination) },
                label = {
                  Text(text = stringResource(id = destination.titleTextId))
                },
                icon = {
                  if (selected) {
                    Icon(
                      imageVector = destination.selectedIcon,
                      contentDescription = stringResource(
                        id = R.string.top_level_navigation_content_description_selected,
                        stringResource(id = destination.titleTextId),
                      ),
                    )
                  } else {
                    Icon(
                      imageVector = destination.unselectedIcon,
                      contentDescription = stringResource(
                        id = R.string.top_level_navigation_content_description_unselected,
                        stringResource(id = destination.titleTextId),
                      ),
                    )
                  }
                },
              )
            }
          }

          NetworkStatusIndicator(networkState = networkState)
        }
      },
    ) { innerPadding ->
      CompositionLocalProvider(
        LocalBottomNavigationPadding provides innerPadding.calculateBottomPadding(),
      ) {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(innerPadding),
        ) {
          when (uiState) {
            is MainUiState.Completed -> ScenePeekNavHost(
              state = state,
            )
            MainUiState.Loading -> LoadingContent()
          }
        }
      }
    }
  }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) = this?.hierarchy?.any {
  it.hasRoute(route)
} ?: false
