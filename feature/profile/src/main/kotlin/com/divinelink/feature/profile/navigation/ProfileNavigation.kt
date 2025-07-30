package com.divinelink.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.profile.ProfileScreen

fun NavGraphBuilder.profileScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.ProfileRoute> {
    ProfileScreen(
      onNavigate = onNavigate,
    )
  }
}
