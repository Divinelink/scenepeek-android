package com.divinelink.feature.settings.navigation.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.about.AboutSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object AboutSettingsRoute

fun NavController.navigateToAboutSettings() = navigate(route = AboutSettingsRoute)

fun NavGraphBuilder.aboutSettingsScreen(onNavigateUp: () -> Unit) {
  composable<AboutSettingsRoute> {
    AboutSettingsScreen(
      onNavigateUp = onNavigateUp,
    )
  }
}
