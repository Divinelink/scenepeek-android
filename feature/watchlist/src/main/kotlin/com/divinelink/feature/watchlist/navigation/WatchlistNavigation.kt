package com.divinelink.feature.watchlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.feature.watchlist.WatchlistScreen
import kotlinx.serialization.Serializable

@Serializable
object WatchlistRoute

fun NavController.navigateToWatchlist(navOptions: NavOptions) = navigate(
  route = WatchlistRoute,
  navOptions = navOptions,
)

fun NavGraphBuilder.watchlistScreen(
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
) {
  composable<WatchlistRoute> {
    WatchlistScreen(
      onNavigateToMediaDetails = onNavigateToDetails,
      onNavigateToTMDBLogin = onNavigateToTMDBLogin,
    )
  }
}
