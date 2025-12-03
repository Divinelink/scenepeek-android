package com.divinelink.feature.settings.navigation.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.details.DetailPreferencesSettingsScreen

fun NavController.navigateToDetailsPreferenceSettings() = navigate(
  route = Navigation.DetailsPreferencesSettingsRoute,
)

fun NavGraphBuilder.detailsPreferencesSettingsScreen(onNavigateUp: () -> Unit) {
  composable<Navigation.DetailsPreferencesSettingsRoute> {
    DetailPreferencesSettingsScreen(
      animatedVisibilityScope = this,
      onNavigateUp = onNavigateUp,
    )
  }
}
