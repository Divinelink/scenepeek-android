package com.divinelink.feature.settings.navigation.settings

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.SettingsScreen

fun NavController.navigateToSettings() = navigate(route = Navigation.SettingsRoute)

fun NavGraphBuilder.settingsScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.SettingsRoute>(
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
      animatedVisibilityScope = this,
      onNavigate = onNavigate,
    )
  }
}
