package com.divinelink.feature.settings.navigation.account

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.settings.app.account.AccountSettingsScreen

fun NavController.navigateToAccountSettings() = navigate(route = Navigation.AccountSettingsRoute)

fun NavGraphBuilder.accountSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  onNavigate: (Navigation) -> Unit,
) {
  composable<Navigation.AccountSettingsRoute> {
    AccountSettingsScreen(
      onNavigate = onNavigate,
      sharedTransitionScope = sharedTransitionScope,
      animatedVisibilityScope = this@composable,
    )
  }
}
