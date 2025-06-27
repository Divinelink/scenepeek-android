package com.divinelink.core.scaffold

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
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
import com.divinelink.core.navigation.route.navigateToHome
import com.divinelink.core.navigation.route.navigateToProfile
import com.divinelink.core.navigation.route.navigateToSearchFromTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberScenePeekAppState(
  networkMonitor: NetworkMonitor,
  onboardingManager: OnboardingManager,
  navigationProvider: List<NavGraphExtension>,
  scope: CoroutineScope = rememberCoroutineScope(),
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  navController: NavHostController = rememberNavController(),
): ScenePeekAppState = remember(networkMonitor, scope) {
  ScenePeekAppState(
    scope = scope,
    navController = navController,
    navigationExtension = navigationProvider,
    networkMonitor = networkMonitor,
    onboardingManager = onboardingManager,
    snackbarHostState = snackbarHostState,
  )
}

@Stable
class ScenePeekAppState internal constructor(
  val navController: NavHostController,
  val scope: CoroutineScope,
  val navigationExtension: List<NavGraphExtension>,
  val snackbarHostState: SnackbarHostState,
  networkMonitor: NetworkMonitor,
  onboardingManager: OnboardingManager, // This could go on MainViewModel
) {
  val currentDestination: NavDestination?
    @Composable get() = navController.currentBackStackEntryAsState().value?.destination

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

  lateinit var sharedTransitionScope: SharedTransitionScope

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
      TopLevelDestination.PROFILE -> navController.navigateToProfile(topLevelNavOptions)
      TopLevelDestination.SEARCH -> navController.navigateToSearchFromTab(topLevelNavOptions)
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
