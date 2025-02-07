package com.divinelink.feature.settings.navigation.settings

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute

fun NavController.navigateToSettings() = navigate(route = SettingsRoute)

fun NavGraphBuilder.settingsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToAppearanceSettings: () -> Unit,
  onNavigateToAccountSettings: () -> Unit,
  onNavigateToDetailPreferencesSettings: () -> Unit,
  onNavigateToLinkHandling: () -> Unit,
  onNavigateToAboutSettings: () -> Unit,
) {
  composable<SettingsRoute>(
    enterTransition = {
      slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
      )
    },
    exitTransition = {
      slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
      )
    },
  ) {
    SettingsScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToAppearanceSettings = onNavigateToAppearanceSettings,
      onNavigateToAccountSettings = onNavigateToAccountSettings,
      onNavigateToDetailPreferencesSettings = onNavigateToDetailPreferencesSettings,
      onNavigateToLinkHandling = onNavigateToLinkHandling,
      onNavigateToAboutSettings = onNavigateToAboutSettings,
    )
  }
}
