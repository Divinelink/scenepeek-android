package com.divinelink.feature.settings.navigation.appearance

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsScreen

fun NavController.navigateToAppearanceSettings() = navigate(
  route = Navigation.AppearanceSettingsRoute,
)

fun NavGraphBuilder.appearanceSettingsScreen(onNavigateUp: () -> Unit) {
  composable<Navigation.AppearanceSettingsRoute> {
    AppearanceSettingsScreen(
      animatedVisibilityScope = this,
      onNavigateUp = onNavigateUp,
    )
  }
}
