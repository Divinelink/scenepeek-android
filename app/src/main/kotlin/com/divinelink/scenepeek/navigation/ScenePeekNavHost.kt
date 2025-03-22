package com.divinelink.scenepeek.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.divinelink.feature.credits.navigation.creditsScreen
import com.divinelink.feature.credits.navigation.navigateToCredits
import com.divinelink.feature.details.navigation.detailsScreen
import com.divinelink.feature.details.navigation.navigateToDetails
import com.divinelink.feature.details.navigation.navigateToPerson
import com.divinelink.feature.details.navigation.personScreen
import com.divinelink.feature.settings.navigation.about.aboutSettingsScreen
import com.divinelink.feature.settings.navigation.about.navigateToAboutSettings
import com.divinelink.feature.settings.navigation.account.accountSettingsScreen
import com.divinelink.feature.settings.navigation.account.jellyseerrSettingsScreen
import com.divinelink.feature.settings.navigation.account.navigateToAccountSettings
import com.divinelink.feature.settings.navigation.account.navigateToJellyseerrSettings
import com.divinelink.feature.settings.navigation.appearance.appearanceSettingsScreen
import com.divinelink.feature.settings.navigation.appearance.navigateToAppearanceSettings
import com.divinelink.feature.settings.navigation.details.detailsPreferencesSettingsScreen
import com.divinelink.feature.settings.navigation.details.navigateToDetailsPreferenceSettings
import com.divinelink.feature.settings.navigation.links.linkHandlingSettingsScreen
import com.divinelink.feature.settings.navigation.links.navigateToLinkHandlingSettings
import com.divinelink.feature.settings.navigation.settings.navigateToSettings
import com.divinelink.feature.settings.navigation.settings.settingsScreen
import com.divinelink.feature.tmdb.auth.navigateToTMDBAuth
import com.divinelink.feature.tmdb.auth.tmdbAuthScreen
import com.divinelink.feature.watchlist.navigation.watchlistScreen
import com.divinelink.scenepeek.home.navigation.HomeRoute
import com.divinelink.scenepeek.home.navigation.homeScreen
import com.divinelink.scenepeek.ui.ScenePeekAppState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ScenePeekNavHost(state: ScenePeekAppState) {
  val navController = state.navController

  SharedTransitionLayout {
    NavHost(
      navController = navController,
      startDestination = HomeRoute,
      enterTransition = {
        fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
      },
      exitTransition = {
        fadeOut(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
      },
    ) {
      homeScreen(
        onNavigateToSettings = navController::navigateToSettings,
        onNavigateToDetails = navController::navigateToDetails,
        onNavigateToPerson = navController::navigateToPerson,
      )

      personScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToDetails = navController::navigateToDetails,
      )

      // Settings screens
      settingsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToAccountSettings = navController::navigateToAccountSettings,
        onNavigateToAppearanceSettings = navController::navigateToAppearanceSettings,
        onNavigateToDetailPreferencesSettings = navController::navigateToDetailsPreferenceSettings,
        onNavigateToLinkHandling = navController::navigateToLinkHandlingSettings,
        onNavigateToAboutSettings = navController::navigateToAboutSettings,
      )

      accountSettingsScreen(
        sharedTransitionScope = this@SharedTransitionLayout,
        onNavigateUp = navController::navigateUp,
        onNavigateToTMDBAuth = navController::navigateToTMDBAuth,
        onNavigateToJellyseerrSettings = navController::navigateToJellyseerrSettings,
      )

      jellyseerrSettingsScreen(
        sharedTransitionScope = this@SharedTransitionLayout,
        onNavigateUp = navController::navigateUp,
      )

      appearanceSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )

      detailsPreferencesSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )

      linkHandlingSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )

      aboutSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
      // End of settings screens

      detailsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToDetails = navController::navigateToDetails,
        onNavigateToCredits = navController::navigateToCredits,
        onNavigateToPerson = navController::navigateToPerson,
        onNavigateToAccountSettings = navController::navigateToAccountSettings,
      )

      creditsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToPerson = navController::navigateToPerson,
      )

      watchlistScreen(
        onNavigateToAccountSettings = navController::navigateToAccountSettings,
        onNavigateToDetails = navController::navigateToDetails,
      )

      tmdbAuthScreen(navController::navigateUp)
    }
  }
}
