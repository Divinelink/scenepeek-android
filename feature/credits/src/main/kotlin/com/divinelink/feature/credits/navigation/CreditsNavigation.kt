package com.divinelink.feature.credits.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.CreditsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.core.navigation.route.map
import com.divinelink.feature.credits.ui.CreditsScreen

fun NavController.navigateToCredits(route: CreditsRoute) = navigate(route = route)

fun NavGraphBuilder.creditsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToPerson: (PersonRoute) -> Unit,
) {
  composable<CreditsRoute> {
    CreditsScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToPersonDetails = { onNavigateToPerson(it.map()) },
    )
  }
}
