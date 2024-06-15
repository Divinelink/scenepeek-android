package com.andreolas.movierama.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.andreolas.movierama.home.ui.HomeScreen
import com.divinelink.ui.screens.NavGraphs
import com.divinelink.ui.screens.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.spec.Route

@Composable
fun AppNavHost(
  startRoute: Route,
  navController: NavHostController,
) {
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
        }
      )
    }
  )
}
