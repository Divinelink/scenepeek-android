package com.divinelink.feature.settings.navigation.account

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsScreen

fun NavController.navigateToJellyseerrSettings(withNavigationBar: Boolean) = navigate(
  route = Navigation.JellyseerrSettingsRoute(withNavigationBar),
)

fun NavGraphBuilder.jellyseerrSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  onNavigateUp: () -> Unit,
) {
  composable<Navigation.JellyseerrSettingsRoute> {
    val route = it.toRoute<Navigation.JellyseerrSettingsRoute>()

    JellyseerrSettingsScreen(
      sharedTransitionScope = sharedTransitionScope,
      onNavigateUp = onNavigateUp,
      withNavigationBar = route.withNavigationBar,
    )
  }
}
