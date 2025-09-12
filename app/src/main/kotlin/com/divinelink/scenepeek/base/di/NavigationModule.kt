package com.divinelink.scenepeek.base.di

import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.AccountSettingsRoute
import com.divinelink.core.navigation.route.Navigation.AppearanceSettingsRoute
import com.divinelink.core.navigation.route.Navigation.JellyseerrSettingsRoute
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.feature.add.to.account.list.navigation.addToListScreen
import com.divinelink.feature.add.to.account.modal.navigation.defaultMediaActionMenu
import com.divinelink.feature.credits.navigation.creditsScreen
import com.divinelink.feature.details.navigation.detailsScreen
import com.divinelink.feature.details.navigation.personScreen
import com.divinelink.feature.lists.create.ui.navigation.createListScreen
import com.divinelink.feature.lists.create.ui.navigation.editListScreen
import com.divinelink.feature.lists.details.ui.navigation.listDetailsScreen
import com.divinelink.feature.lists.user.navigation.listsScreen
import com.divinelink.feature.onboarding.navigation.fullscreenOnboarding
import com.divinelink.feature.onboarding.navigation.modalOnboarding
import com.divinelink.feature.profile.navigation.profileScreen
import com.divinelink.feature.requests.ui.navigation.requestsScreen
import com.divinelink.feature.search.navigation.searchScreen
import com.divinelink.feature.settings.navigation.about.aboutSettingsScreen
import com.divinelink.feature.settings.navigation.account.accountSettingsScreen
import com.divinelink.feature.settings.navigation.account.jellyseerrSettingsScreen
import com.divinelink.feature.settings.navigation.appearance.appearanceSettingsScreen
import com.divinelink.feature.settings.navigation.details.detailsPreferencesSettingsScreen
import com.divinelink.feature.settings.navigation.links.linkHandlingSettingsScreen
import com.divinelink.feature.settings.navigation.settings.settingsScreen
import com.divinelink.feature.tmdb.auth.tmdbAuthScreen
import com.divinelink.feature.user.data.navigation.userDataScreen
import com.divinelink.feature.webview.webViewScreen
import com.divinelink.scenepeek.home.navigation.findNavigation
import com.divinelink.scenepeek.home.navigation.homeScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val navigationModule = module {

  single<NavGraphExtension>(named<Navigation.HomeRoute>()) {
    { navController, _ ->
      homeScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

// Person Navigation
  single<NavGraphExtension>(named<Navigation.PersonRoute>()) {
    { navController, _ ->
      personScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

// Details Navigation
  single<NavGraphExtension>(named<Navigation.DetailsRoute>()) {
    { navController, _ ->
      detailsScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

// Search Navigation
  single<NavGraphExtension>(named<Navigation.SearchRoute>()) {
    { navController, _ ->
      searchScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

// Settings Navigation
  single<NavGraphExtension>(named<Navigation.SettingsRoute>()) {
    { navController, _ ->
      settingsScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

// Account Settings Navigation
  single<NavGraphExtension>(named<AccountSettingsRoute>()) {
    { navController, transitionScope ->
      accountSettingsScreen(
        sharedTransitionScope = transitionScope,
        onNavigate = navController::findNavigation,
      )
    }
  }

// Jellyseerr Settings Navigation
  single<NavGraphExtension>(named<JellyseerrSettingsRoute>()) {
    { navController, transitionScope ->
      jellyseerrSettingsScreen(
        sharedTransitionScope = transitionScope,
        onNavigateUp = navController::navigateUp,
      )
    }
  }

  // Appearance Settings Navigation
  single<NavGraphExtension>(named<AppearanceSettingsRoute>()) {
    { navController, _ ->
      appearanceSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

  // Details Preferences Settings Navigation
  single<NavGraphExtension>(named<Navigation.DetailsPreferencesSettingsRoute>()) {
    { navController, _ ->
      detailsPreferencesSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// Link Handling Settings Navigation
  single<NavGraphExtension>(named<Navigation.LinkHandlingSettingsRoute>()) {
    { navController, _ ->
      linkHandlingSettingsScreen(
        onNavigateUp = navController::navigateUp,
      )
    }
  }

// About Settings Navigation
  single<NavGraphExtension>(named<Navigation.AboutSettingsRoute>()) {
    { navController, _ ->
      aboutSettingsScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // Credits Navigation
  single<NavGraphExtension>(named<Navigation.CreditsRoute>()) {
    { navController, _ ->
      creditsScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // UserData Navigation (Watchlist, Ratings)
  single<NavGraphExtension>(named<Navigation.UserDataRoute>()) {
    { navController, _ ->
      userDataScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // Fullscreen Onboarding Navigation
  single<NavGraphExtension>(named<Navigation.Onboarding.FullScreenRoute>()) {
    { navController, _ ->
      fullscreenOnboarding(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // Modal Onboarding Navigation
  single<NavGraphExtension>(named<Navigation.Onboarding.ModalRoute>()) {
    { navController, _ ->
      modalOnboarding(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // TMDB Auth Navigation
  single<NavGraphExtension>(named<Navigation.TMDBAuthRoute>()) {
    { navController, _ ->
      tmdbAuthScreen(navController::findNavigation)
    }
  }

  // Profile Navigation
  single<NavGraphExtension>(named<Navigation.ProfileRoute>()) {
    { navController, _ ->
      profileScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // User Lists Navigation
  single<NavGraphExtension>(named<Navigation.ListsRoute>()) {
    { navController, _ ->
      listsScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // List Details Navigation
  single<NavGraphExtension>(named<Navigation.ListDetailsRoute>()) {
    { navController, _ ->
      listDetailsScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // Create List Navigation
  single<NavGraphExtension>(named<Navigation.CreateListRoute>()) {
    { navController, _ ->
      createListScreen(
        onNavigateUp = navController::navigateUp,

      )
    }
  }

  // Create List Navigation
  single<NavGraphExtension>(named<Navigation.EditListRoute>()) {
    { navController, _ ->
      editListScreen(
        onNavigateUp = navController::navigateUp,
        onNavigateBackToLists = {
          navController.popBackStack(
            route = Navigation.ListsRoute,
            inclusive = false,
          )
        },
      )
    }
  }

  // Add To List Navigation
  single<NavGraphExtension>(named<Navigation.AddToListRoute>()) {
    { navController, _ ->
      addToListScreen(
        onNavigate = navController::findNavigation,
      )
    }
  }

  // WebView Navigation
  single<NavGraphExtension>(named<Navigation.WebViewRoute>()) {
    { navController, _ ->
      webViewScreen(navController::findNavigation)
    }
  }

  // Requests Navigation
  single<NavGraphExtension>(named<Navigation.JellyseerrRequestsRoute>()) {
    { navController, _ ->
      requestsScreen(navController::findNavigation)
    }
  }

  // Action Menu Navigation
  single<NavGraphExtension>(named<Navigation.ActionMenuRoute.Media>()) {
    { navController, _ ->
      defaultMediaActionMenu(navController::findNavigation)
    }
  }

  single<List<NavGraphExtension>> {
    getAll<NavGraphExtension>()
  }
}
