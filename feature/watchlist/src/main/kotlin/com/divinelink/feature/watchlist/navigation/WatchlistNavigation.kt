package com.divinelink.feature.watchlist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.WatchlistRoute
import com.divinelink.feature.watchlist.WatchlistScreen

fun NavGraphBuilder.watchlistScreen(
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateUp: () -> Unit,
) {
  composable<WatchlistRoute> {
    WatchlistScreen(
      onNavigateToMediaDetails = onNavigateToDetails,
      onNavigateToTMDBLogin = onNavigateToTMDBLogin,
      onNavigateUp = onNavigateUp,
      animatedVisibilityScope = this@composable,
    )
  }
}
