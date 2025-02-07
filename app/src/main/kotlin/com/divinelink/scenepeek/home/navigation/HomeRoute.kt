package com.divinelink.scenepeek.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.scenepeek.home.ui.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions) = navigate(
  route = HomeRoute,
  navOptions = navOptions,
)

fun NavGraphBuilder.homeScreen(
  onNavigateToSettings: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToPerson: (PersonRoute) -> Unit,
) {
  composable<HomeRoute> {
    HomeScreen(
      onNavigateToDetails = onNavigateToDetails,
      onNavigateToSettings = onNavigateToSettings,
      onNavigateToPerson = onNavigateToPerson,
    )
  }
}
