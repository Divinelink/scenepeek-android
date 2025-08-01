package com.divinelink.feature.onboarding.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.onboarding.ui.OnboardingScreen

fun NavGraphBuilder.onboardingScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.OnboardingRoute> {
    BackHandler(enabled = true) {
      // Disable back button
    }

    OnboardingScreen(
      onNavigate = onNavigate,
    )
  }
}
