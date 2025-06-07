package com.divinelink.feature.settings.navigation.account

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrSettingsRoute(val withNavigationBar: Boolean)

fun NavController.navigateToJellyseerrSettings(withNavigationBar: Boolean) = navigate(
  route = JellyseerrSettingsRoute(withNavigationBar),
)

fun NavGraphBuilder.jellyseerrSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  onNavigateUp: () -> Unit,
) {
  composable<JellyseerrSettingsRoute> {
    val route = it.toRoute<JellyseerrSettingsRoute>()

    JellyseerrSettingsScreen(
      sharedTransitionScope = sharedTransitionScope,
      onNavigateUp = onNavigateUp,
      withNavigationBar = route.withNavigationBar,
    )
  }
}
