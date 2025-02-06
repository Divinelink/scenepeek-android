package com.divinelink.feature.settings.navigation.links

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.links.LinkHandlingSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object LinkHandlingSettingsRoute

fun NavController.navigateToLinkHandlingSettings() = navigate(LinkHandlingSettingsRoute)

fun NavGraphBuilder.linkHandlingSettingsScreen(onNavigateUp: () -> Unit) {
  composable<LinkHandlingSettingsRoute> {
    LinkHandlingSettingsScreen(
      navigateUp = onNavigateUp,
    )
  }
}
