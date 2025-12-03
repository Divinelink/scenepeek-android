package com.divinelink.feature.settings.navigation.links

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.links.LinkHandlingSettingsScreen

fun NavController.navigateToLinkHandlingSettings() = navigate(Navigation.LinkHandlingSettingsRoute)

fun NavGraphBuilder.linkHandlingSettingsScreen(onNavigateUp: () -> Unit) {
  composable<Navigation.LinkHandlingSettingsRoute> {
    LinkHandlingSettingsScreen(
      animatedVisibilityScope = this,
      navigateUp = onNavigateUp,
    )
  }
}
