package com.divinelink.feature.settings.navigation.updates

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.updates.AppUpdatesScreen

fun NavController.navigateToAppUpdates() = navigate(route = Navigation.AppUpdatesSettingsRoute)

fun NavGraphBuilder.appUpdatesScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.AppUpdatesSettingsRoute> {
    AppUpdatesScreen(
      animatedVisibilityScope = this,
      onNavigate = onNavigate,
    )
  }
}
