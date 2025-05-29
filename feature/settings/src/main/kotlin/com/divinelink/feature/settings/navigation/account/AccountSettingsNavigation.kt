package com.divinelink.feature.settings.navigation.account

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.account.AccountSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object AccountSettingsRoute

fun NavController.navigateToAccountSettings() = navigate(route = AccountSettingsRoute)

fun NavGraphBuilder.accountSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  onNavigateUp: () -> Unit,
  onNavigateToTMDBAuth: () -> Unit,
  onNavigateToJellyseerrSettings: () -> Unit,
) {
  composable<AccountSettingsRoute> {
    AccountSettingsScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToJellyseerrSettingsScreen = onNavigateToJellyseerrSettings,
      onNavigateToTMDBAuth = onNavigateToTMDBAuth,
      sharedTransitionScope = sharedTransitionScope,
      animatedVisibilityScope = this@composable,
    )
  }
}
