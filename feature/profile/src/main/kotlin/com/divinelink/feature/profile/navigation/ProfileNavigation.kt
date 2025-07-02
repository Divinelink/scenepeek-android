package com.divinelink.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.model.user.data.UserDataSection
import com.divinelink.core.navigation.route.ProfileRoute
import com.divinelink.feature.profile.ProfileScreen

fun NavGraphBuilder.profileScreen(
  onNavigateToUserData: (UserDataSection) -> Unit,
  onNavigateToLists: () -> Unit,
  onNavigateToTMDBAuth: () -> Unit,
) {
  composable<ProfileRoute> {
    ProfileScreen(
      onNavigateToUserData = onNavigateToUserData,
      onNavigateToLists = onNavigateToLists,
      onNavigateToTMDBAuth = onNavigateToTMDBAuth,
    )
  }
}
