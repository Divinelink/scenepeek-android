package com.divinelink.feature.settings.navigation.account


import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object JellyseerrSettingsRoute

fun NavController.navigateToJellyseerrSettings() = navigate(route = JellyseerrSettingsRoute)

fun NavGraphBuilder.jellyseerrSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  onNavigateUp: () -> Unit,
) {
  composable<JellyseerrSettingsRoute> {
    JellyseerrSettingsScreen(
      sharedTransitionScope = sharedTransitionScope,
      onNavigateUp = onNavigateUp,
      animatedVisibilityScope = this@composable,
    )
  }
}
