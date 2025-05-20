package com.divinelink.scenepeek.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.feature.watchlist.navigation.navigateToWatchlist
import com.divinelink.scenepeek.home.navigation.navigateToHome
import com.divinelink.scenepeek.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberScenePeekAppState(
  networkMonitor: NetworkMonitor,
  onboardingManager: OnboardingManager,
  scope: CoroutineScope = rememberCoroutineScope(),
  navController: NavHostController = rememberNavController(),
): ScenePeekAppState = remember(networkMonitor, scope) {
  ScenePeekAppState(
    scope = scope,
    navController = navController,
    networkMonitor = networkMonitor,
    onboardingManager = onboardingManager,
  )
}

@Stable
class ScenePeekAppState(
  val navController: NavHostController,
  val scope: CoroutineScope,
  networkMonitor: NetworkMonitor,
  onboardingManager: OnboardingManager,
) {
  val currentDestination: NavDestination?
    @Composable get() = navController.currentBackStackEntryAsState().value?.destination

  val showBottomNavigation = MutableStateFlow(true)

  val isOffline = networkMonitor.isOnline
    .map(Boolean::not)
    .stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
      initialValue = false,
    )

  val shouldShowOnboarding = onboardingManager.shouldShowOnboarding
    .stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
      initialValue = false,
    )

  val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

  fun setBottomNavigationVisibility(visible: Boolean) {
    showBottomNavigation.value = visible
  }

  fun navigateToTopLevelDestination(destination: TopLevelDestination) {
    val topLevelNavOptions = navOptions {
      popUpTo(navController.graph.findStartDestination().id) {
        saveState = true
      }
      // Avoid multiple copies of the same destination when re-selecting the same item
      launchSingleTop = true
      // Restore state when re-selecting a previously selected item
      restoreState = true
    }

    when (destination) {
      TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
      TopLevelDestination.WATCHLIST -> navController.navigateToWatchlist(topLevelNavOptions)
    }

    navController.popBackStack(
      route = destination.route,
      inclusive = false,
    )
  }

  companion object {
    val SUBSCRIPTION_TIMEOUT = 5.seconds.inWholeMilliseconds
  }
}
