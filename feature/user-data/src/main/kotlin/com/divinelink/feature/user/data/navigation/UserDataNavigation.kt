package com.divinelink.feature.user.data.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.UserDataRoute
import com.divinelink.feature.user.data.UserDataScreen

fun NavGraphBuilder.userDataScreen(onNavigate: (Navigation) -> Unit) {
  composable<UserDataRoute> {
    UserDataScreen(
      onNavigate = onNavigate,
    )
  }
}
