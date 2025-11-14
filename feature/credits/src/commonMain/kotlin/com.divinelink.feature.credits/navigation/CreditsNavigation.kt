package com.divinelink.feature.credits.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.credits.ui.CreditsScreen

fun NavController.navigateToCredits(route: Navigation.CreditsRoute) = navigate(route = route)

fun NavGraphBuilder.creditsScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.CreditsRoute> {
    CreditsScreen(onNavigate = onNavigate)
  }
}
