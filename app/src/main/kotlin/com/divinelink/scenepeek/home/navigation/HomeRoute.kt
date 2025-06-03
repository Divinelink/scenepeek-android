package com.divinelink.scenepeek.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.HomeRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.scenepeek.home.ui.HomeScreen

fun NavGraphBuilder.homeScreen(
  onNavigateToSettings: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToPerson: (PersonRoute) -> Unit,
  onNavigateToSearch: () -> Unit,
) {
  composable<HomeRoute> {
    HomeScreen(
      onNavigateToDetails = onNavigateToDetails,
      onNavigateToSettings = onNavigateToSettings,
      onNavigateToPerson = onNavigateToPerson,
      onNavigateToSearch = onNavigateToSearch,
      animatedVisibilityScope = this@composable,
    )
  }
}
