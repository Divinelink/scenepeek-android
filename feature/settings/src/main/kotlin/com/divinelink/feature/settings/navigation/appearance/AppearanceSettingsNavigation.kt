package com.divinelink.feature.settings.navigation.appearance

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object AppearanceSettingsRoute

fun NavController.navigateToAppearanceSettings() = navigate(route = AppearanceSettingsRoute)

fun NavGraphBuilder.appearanceSettingsScreen(onNavigateUp: () -> Unit) {
  composable<AppearanceSettingsRoute> {
    AppearanceSettingsScreen(
      onNavigateUp = onNavigateUp,
    )
  }
}
