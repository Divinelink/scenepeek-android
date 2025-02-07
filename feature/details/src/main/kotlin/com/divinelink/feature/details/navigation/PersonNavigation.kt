package com.divinelink.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.feature.details.person.ui.PersonScreen

fun NavController.navigateToPerson(route: PersonRoute) = navigate(route = route)

fun NavGraphBuilder.personScreen(
  onNavigateUp: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
) {
  composable<PersonRoute> {
    PersonScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToDetails = onNavigateToDetails,
    )
  }
}
