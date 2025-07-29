package com.divinelink.scenepeek.base.di

import com.divinelink.core.navigation.NavigationQualifier
import com.divinelink.core.navigation.route.ListsRoute
import com.divinelink.core.navigation.route.navigateToAddToList
import com.divinelink.core.navigation.route.navigateToCreateList
import com.divinelink.core.navigation.route.navigateToDetails
import com.divinelink.core.navigation.route.navigateToEditList
import com.divinelink.core.navigation.route.navigateToListDetails
import com.divinelink.core.navigation.route.navigateToLists
import com.divinelink.core.navigation.route.navigateToPerson
import com.divinelink.core.navigation.route.navigateToSearchFromHome
import com.divinelink.core.navigation.route.navigateToUserData
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.feature.add.to.account.list.navigation.addToListScreen
import com.divinelink.feature.credits.navigation.creditsScreen
import com.divinelink.feature.credits.navigation.navigateToCredits
import com.divinelink.feature.details.navigation.detailsScreen
import com.divinelink.feature.details.navigation.personScreen
import com.divinelink.feature.lists.create.ui.navigation.createListScreen
import com.divinelink.feature.lists.create.ui.navigation.editListScreen
import com.divinelink.feature.lists.details.ui.navigation.listDetailsScreen
import com.divinelink.feature.lists.user.navigation.listsScreen
import com.divinelink.feature.onboarding.navigation.onboardingScreen
import com.divinelink.feature.profile.navigation.profileScreen
import com.divinelink.feature.search.navigation.searchScreen
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
import com.divinelink.feature.user.data.navigation.userDataScreen
import com.divinelink.scenepeek.home.navigation.homeScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val navigationModule = module {

  single<NavGraphExtension>(named(NavigationQualifier.Home)) {
    { navController, _ ->
      homeScreen(
        onNavigateToSettings = navController::navigateToSettings,
        onNavigateToDetails = navController::navigateToDetails,
        onNavigateToPerson = navController::navigateToPerson,
        onNavigateToSearch = navController::navigateToSearchFromHome,
      )
    }
  }

// Person Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Person)) {
    { navController, _ ->
      personScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToDetails = navController::navigateToDetails,
      )
    }
  }

// Details Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Details)) {
    { navController, _ ->
      detailsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToDetails = navController::navigate,
        onNavigateToCredits = navController::navigateToCredits,
        onNavigateToPerson = navController::navigateToPerson,
        onNavigateToTMDBLogin = navController::navigateToTMDBAuth,
        onNavigateToAddToList = navController::navigateToAddToList,
      )
    }
  }

// Search Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Search)) {
    { navController, _ ->
      searchScreen(
        onNavigateToSettings = navController::navigateToSettings,
        onNavigateToDetails = navController::navigateToDetails,
        onNavigateToPerson = navController::navigateToPerson,
      )
    }
  }

// Settings Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Settings)) {
    { navController, _ ->
      settingsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToAccountSettings = navController::navigateToAccountSettings,
        onNavigateToAppearanceSettings = navController::navigateToAppearanceSettings,
        onNavigateToDetailPreferencesSettings = navController::navigateToDetailsPreferenceSettings,
        onNavigateToLinkHandling = navController::navigateToLinkHandlingSettings,
        onNavigateToAboutSettings = navController::navigateToAboutSettings,
      )
    }
  }

// Account Settings Navigation
  single<NavGraphExtension>(named(NavigationQualifier.AccountSettings)) {
    { navController, transitionScope ->
      accountSettingsScreen(
        sharedTransitionScope = transitionScope,
        onNavigateUp = navController::navigateUp,
        onNavigateToTMDBAuth = navController::navigateToTMDBAuth,
        onNavigateToJellyseerrSettings = { navController.navigateToJellyseerrSettings(true) },
      )
    }
  }

// Jellyseerr Settings Navigation
  single<NavGraphExtension>(named(NavigationQualifier.JellyseerrSettings)) {
    { navController, transitionScope ->
      jellyseerrSettingsScreen(
        sharedTransitionScope = transitionScope,
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// Appearance Settings Navigation
  single<NavGraphExtension>(named(NavigationQualifier.AppearanceSettings)) {
    { navController, _ ->
      appearanceSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// Details Preferences Settings Navigation
  single<NavGraphExtension>(named(NavigationQualifier.DetailsPreferencesSettings)) {
    { navController, _ ->
      detailsPreferencesSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// Link Handling Settings Navigation
  single<NavGraphExtension>(named(NavigationQualifier.LinkHandlingSettings)) {
    { navController, _ ->
      linkHandlingSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// About Settings Navigation
  single<NavGraphExtension>(named(NavigationQualifier.AboutSettings)) {
    { navController, _ ->
      aboutSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// Credits Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Credits)) {
    { navController, _ ->
      creditsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToPerson = navController::navigateToPerson,
      )
    }
  }

// Watchlist Navigation
  single<NavGraphExtension>(named(NavigationQualifier.UserData)) {
    { navController, _ ->
      userDataScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToDetails = navController::navigateToDetails,
        onNavigateToTMDBLogin = navController::navigateToTMDBAuth,
      )
    }
  }

// Onboarding Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Onboarding)) {
    { navController, _ ->
      onboardingScreen(
        onNavigateToJellyseerrSettings = { navController.navigateToJellyseerrSettings(false) },
        onNavigateToTMDBLogin = navController::navigateToTMDBAuth,
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// TMDB Auth Navigation
  single<NavGraphExtension>(named(NavigationQualifier.TMDBAuth)) {
    { navController, _ ->
      tmdbAuthScreen(navController::navigateUp)
    }
  }

  // Profile Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Profile)) {
    { navController, _ ->
      profileScreen(
        onNavigateToUserData = navController::navigateToUserData,
        onNavigateToTMDBAuth = navController::navigateToTMDBAuth,
        onNavigateToLists = navController::navigateToLists,
      )
    }
  }

  // User Lists Navigation
  single<NavGraphExtension>(named(NavigationQualifier.Lists)) {
    { navController, _ ->
      listsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToTMDBLogin = navController::navigateToTMDBAuth,
        onNavigateToList = navController::navigateToListDetails,
        onNavigateToCreateList = navController::navigateToCreateList,
      )
    }
  }

  // List Details Navigation
  single<NavGraphExtension>(named(NavigationQualifier.ListDetails)) {
    { navController, _ ->
      listDetailsScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToDetails = navController::navigateToDetails,
        onNavigateToEdit = navController::navigateToEditList,
        onNavigateToAddToList = navController::navigateToAddToList,
      )
    }
  }

  // Create List Navigation
  single<NavGraphExtension>(named(NavigationQualifier.CreateList)) {
    { navController, _ ->
      createListScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

  // Create List Navigation
  single<NavGraphExtension>(named(NavigationQualifier.EditList)) {
    { navController, _ ->
      editListScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateBackToLists = {
          navController.popBackStack(
            route = ListsRoute,
            inclusive = false,
          )
        },
      )
    }
  }

  // Add To List Navigation
  single<NavGraphExtension>(named(NavigationQualifier.AddToList)) {
    { navController, _ ->
      addToListScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateToTMDBAuth = navController::navigateToTMDBAuth,
        onNavigateToCreateList = navController::navigateToCreateList,
      )
    }
  }

// Collect all navigation extensions
  single<List<NavGraphExtension>> {
    NavigationQualifier.entries.map { qualifier ->
      get<NavGraphExtension>(named(qualifier))
    }
  }

  single<List<NavGraphExtension>> {
    getAll<NavGraphExtension>()
  }
}
