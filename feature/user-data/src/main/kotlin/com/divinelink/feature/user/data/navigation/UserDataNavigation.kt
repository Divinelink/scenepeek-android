package com.divinelink.feature.user.data.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.UserDataRoute
import com.divinelink.feature.user.data.UserDataScreen

fun NavGraphBuilder.userDataScreen(
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToTMDBLogin: () -> Unit,
  onNavigateUp: () -> Unit,
) {
  composable<UserDataRoute> {
    UserDataScreen(
      onNavigateToMediaDetails = onNavigateToDetails,
      onNavigateToTMDBLogin = onNavigateToTMDBLogin,
      onNavigateUp = onNavigateUp,
    )
  }
}
