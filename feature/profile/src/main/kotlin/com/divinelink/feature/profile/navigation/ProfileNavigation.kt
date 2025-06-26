package com.divinelink.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.ProfileRoute
import com.divinelink.feature.profile.ProfileScreen

fun NavGraphBuilder.profileScreen() {
  composable<ProfileRoute> {
    ProfileScreen()
  }
}
