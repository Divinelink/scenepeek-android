package com.divinelink.feature.onboarding.navigation

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
) {
  composable<OnboardingRoute> {
    OnboardingScreen(
      onNavigateToJellyseerrSettings = onNavigateToJellyseerrSettings,
      onNavigateToTMDBLogin = onNavigateToTMDBLogin,
    )
  }
}
