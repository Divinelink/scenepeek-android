package com.divinelink.feature.onboarding.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.OnboardingRoute
import com.divinelink.feature.onboarding.ui.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(
  onNavigateToJellyseerrSettings: () -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateUp: () -> Unit,
) {
  composable<OnboardingRoute> {
    BackHandler(enabled = true) {
      // Disable back button
    }

    OnboardingScreen(
      onNavigateToJellyseerrSettings = onNavigateToJellyseerrSettings,
      onNavigateToTMDBLogin = onNavigateToTMDBLogin,
      onNavigateUp = onNavigateUp,
    )
  }
}
