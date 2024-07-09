package com.andreolas.movierama.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.andreolas.movierama.home.ui.HomeScreen
import com.divinelink.feature.settings.app.account.AccountSettingsScreen
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsScreen
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.JellyseerrSettingsScreenDestination
import com.divinelink.ui.screens.NavGraphs
import com.divinelink.ui.screens.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
  startRoute: Route,
  navController: NavHostController,
) {
  SharedTransitionLayout {
    DestinationsNavHost(
      startRoute = startRoute,
      navController = navController,
      defaultTransitions = DestinationDefaultTransitions(),
      navGraph = NavGraphs.main,
      manualComposableCallsBuilder = {
        composable(
          destination = HomeScreenDestination,
          content = {
            HomeScreen(
              navigator = destinationsNavigator,
            )
          },
        )

        composable(
          destination = AccountSettingsScreenDestination,
          content = {
            AccountSettingsScreen(
              navigator = destinationsNavigator,
              animatedVisibilityScope = this@composable,
            )
          },
        )

        composable(
          destination = JellyseerrSettingsScreenDestination,
          content = {
            JellyseerrSettingsScreen(
              navigator = destinationsNavigator,
              animatedVisibilityScope = this@composable,
            )
          },
        )
      },
    )
  }
}
