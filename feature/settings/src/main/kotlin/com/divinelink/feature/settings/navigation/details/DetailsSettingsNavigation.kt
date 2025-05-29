package com.divinelink.feature.settings.navigation.details

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.details.DetailPreferencesSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object DetailsPreferencesSettingsRoute

fun NavController.navigateToDetailsPreferenceSettings() = navigate(
  route = DetailsPreferencesSettingsRoute,
)

fun NavGraphBuilder.detailsPreferencesSettingsScreen(onNavigateUp: () -> Unit) {
  composable<DetailsPreferencesSettingsRoute> {
    DetailPreferencesSettingsScreen(
      animatedVisibilityScope = this,
      onNavigateUp = onNavigateUp,
    )
  }
}
