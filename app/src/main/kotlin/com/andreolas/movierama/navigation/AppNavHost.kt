package com.andreolas.movierama.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.andreolas.movierama.home.ui.HomeScreen
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.feature.credits.screens.destinations.CreditsScreenDestination
import com.divinelink.feature.credits.ui.CreditsScreen
import com.divinelink.feature.details.media.ui.DetailsScreen
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import com.divinelink.feature.settings.app.account.AccountSettingsScreen
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsScreen
import com.divinelink.feature.settings.screens.destinations.AccountSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.JellyseerrSettingsScreenDestination
import com.divinelink.feature.watchlist.WatchlistScreen
import com.divinelink.ui.screens.NavGraphs
import com.divinelink.ui.screens.destinations.HomeScreenDestination
import com.divinelink.ui.screens.destinations.WatchlistScreenDestination
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

        composable(
          destination = DetailsScreenDestination,
          content = {
            DetailsScreen(
              navigator = destinationsNavigator,
              onNavigateToAccountSettings = {
                destinationsNavigator.navigate(AccountSettingsScreenDestination)
              },
              onNavigateToCredits = {
                destinationsNavigator.navigate(CreditsScreenDestination(it))
              },
            )
          },
        )

        composable(
          destination = CreditsScreenDestination,
          content = {
            CreditsScreen(
              navigator = destinationsNavigator,
              onNavigateToPersonDetails = {
                destinationsNavigator.navigate(
                  PersonScreenDestination(PersonNavArguments(id = it)),
                )
              },
            )
          },
        )

        composable(
          destination = WatchlistScreenDestination,
          content = {
            WatchlistScreen(
              onNavigateToAccountSettings = {
                destinationsNavigator.navigate(AccountSettingsScreenDestination)
              },
              onNavigateToMediaDetails = {
                destinationsNavigator.navigate(DetailsScreenDestination(it))
              },
            )
          },
        )
      },
    )
  }
}
