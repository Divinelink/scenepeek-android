package com.divinelink.feature.settings.navigation.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.about.AboutSettingsScreen

fun NavController.navigateToAboutSettings() = navigate(route = Navigation.AboutSettingsRoute)

fun NavGraphBuilder.aboutSettingsScreen(onNavigateUp: () -> Unit) {
  composable<Navigation.AboutSettingsRoute> {
    AboutSettingsScreen(
      animatedVisibilityScope = this,
      onNavigateUp = onNavigateUp,
    )
  }
}
