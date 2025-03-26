package com.divinelink.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.CreditsRoute
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.feature.details.media.ui.DetailsScreen

fun NavController.navigateToDetails(route: DetailsRoute) = navigate(route = route)

fun NavGraphBuilder.detailsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToCredits: (CreditsRoute) -> Unit,
  onNavigateToPerson: (PersonRoute) -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
) {
  composable<DetailsRoute> {
    DetailsScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToDetails = onNavigateToDetails,
      onNavigateToCredits = onNavigateToCredits,
      onNavigateToPerson = onNavigateToPerson,
      onNavigateToTMDBLogin = onNavigateToTMDBLogin,
    )
  }
}
