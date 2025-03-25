package com.divinelink.feature.onboarding.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.onboarding.ui.OnboardingScreen
import kotlinx.serialization.Serializable

@Serializable
data object OnboardingRoute

fun NavController.navigateToOnboarding() = navigate(route = OnboardingRoute)

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
