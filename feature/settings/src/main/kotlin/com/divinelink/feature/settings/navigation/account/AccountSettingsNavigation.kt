package com.divinelink.feature.settings.navigation.account

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.divinelink.feature.settings.app.account.AccountSettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object AccountSettingsRoute

fun NavController.navigateToAccountSettings() = navigate(route = AccountSettingsRoute)

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.accountSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  onNavigateUp: () -> Unit,
  onNavigateToJellyseerrSettings: () -> Unit,
) {
  composable<AccountSettingsRoute> {
    AccountSettingsScreen(
      onNavigateUp = onNavigateUp,
      onNavigateToJellyseerrSettingsScreen = onNavigateToJellyseerrSettings,
      sharedTransitionScope = sharedTransitionScope,
      animatedVisibilityScope = this@composable,
    )
  }
}
