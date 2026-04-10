package com.divinelink.scenepeek.di

import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.AccountSettingsRoute
import com.divinelink.core.navigation.route.Navigation.AppearanceSettingsRoute
import com.divinelink.core.navigation.route.Navigation.JellyseerrSettingsRoute
import com.divinelink.core.scaffold.NavEntryProvider
import com.divinelink.feature.add.to.account.list.navigation.addToListScreen
import com.divinelink.feature.add.to.account.modal.navigation.defaultMediaActionMenu
import com.divinelink.feature.collections.ui.navigation.collectionsScreen
import com.divinelink.feature.credits.navigation.creditsScreen
import com.divinelink.feature.details.navigation.detailsScreen
import com.divinelink.feature.details.navigation.personScreen
import com.divinelink.feature.details.navigation.posterScreen
import com.divinelink.feature.discover.ui.navigation.discoverScreen
import com.divinelink.feature.episode.ui.navigation.episodeScreen
import com.divinelink.feature.home.navigation.homeScreen
import com.divinelink.feature.lists.create.ui.navigation.createListScreen
import com.divinelink.feature.lists.create.ui.navigation.editListScreen
import com.divinelink.feature.lists.details.ui.navigation.listDetailsScreen
import com.divinelink.feature.lists.user.navigation.listsScreen
import com.divinelink.feature.media.lists.navigation.mediaListsScreen
import com.divinelink.feature.onboarding.navigation.fullscreenOnboarding
import com.divinelink.feature.onboarding.navigation.modalOnboarding
import com.divinelink.feature.profile.navigation.profileScreen
import com.divinelink.feature.requests.ui.navigation.requestsScreen
import com.divinelink.feature.search.navigation.searchScreen
import com.divinelink.feature.season.ui.navigation.seasonScreen
import com.divinelink.feature.settings.navigation.about.aboutSettingsScreen
import com.divinelink.feature.settings.navigation.account.accountSettingsScreen
import com.divinelink.feature.settings.navigation.account.jellyseerrSettingsScreen
import com.divinelink.feature.settings.navigation.appearance.appearanceSettingsScreen
import com.divinelink.feature.settings.navigation.details.detailsPreferencesSettingsScreen
import com.divinelink.feature.settings.navigation.links.linkHandlingSettingsScreen
import com.divinelink.feature.settings.navigation.settings.settingsScreen
import com.divinelink.feature.settings.navigation.updates.appUpdatesScreen
import com.divinelink.feature.tmdb.auth.tmdbAuthScreen
import com.divinelink.feature.updater.ui.navigation.updaterScreen
import com.divinelink.feature.user.data.navigation.userDataScreen
import com.divinelink.feature.webview.webViewScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val navigationModule = module {

  single<NavEntryProvider>(named<Navigation.HomeRoute>()) {
    { onNavigate ->
      homeScreen(onNavigate = onNavigate)
    }
  }

// Person Navigation
  single<NavEntryProvider>(named<Navigation.PersonRoute>()) {
    { onNavigate ->
      personScreen(onNavigate = onNavigate)
    }
  }

// Details Navigation
  single<NavEntryProvider>(named<Navigation.DetailsRoute>()) {
    { onNavigate ->
      detailsScreen(onNavigate = onNavigate)
    }
  }

// Search Navigation
  single<NavEntryProvider>(named<Navigation.SearchRoute>()) {
    { onNavigate ->
      searchScreen(onNavigate = onNavigate)
    }
  }

// Settings Navigation
  single<NavEntryProvider>(named<Navigation.SettingsRoute>()) {
    { onNavigate ->
      settingsScreen(onNavigate = onNavigate)
    }
  }

// Account Settings Navigation
  single<NavEntryProvider>(named<AccountSettingsRoute>()) {
    { onNavigate ->
      accountSettingsScreen(onNavigate = onNavigate)
    }
  }

// Jellyseerr Settings Navigation
  single<NavEntryProvider>(named<JellyseerrSettingsRoute>()) {
    { onNavigate ->
      jellyseerrSettingsScreen(onNavigate = onNavigate)
    }
  }

  // Appearance Settings Navigation
  single<NavEntryProvider>(named<AppearanceSettingsRoute>()) {
    { onNavigate ->
      appearanceSettingsScreen(onNavigate = onNavigate)
    }
  }

  // Details Preferences Settings Navigation
  single<NavEntryProvider>(named<Navigation.DetailsPreferencesSettingsRoute>()) {
    { onNavigate ->
      detailsPreferencesSettingsScreen(onNavigate = onNavigate)
    }
  }

// Link Handling Settings Navigation
  single<NavEntryProvider>(named<Navigation.LinkHandlingSettingsRoute>()) {
    { onNavigate ->
      linkHandlingSettingsScreen(onNavigate = onNavigate)
    }
  }

// About Settings Navigation
  single<NavEntryProvider>(named<Navigation.AboutSettingsRoute>()) {
    { onNavigate ->
      aboutSettingsScreen(onNavigate = onNavigate)
    }
  }

  // Credits Navigation
  single<NavEntryProvider>(named<Navigation.CreditsRoute>()) {
    { onNavigate ->
      creditsScreen(onNavigate = onNavigate)
    }
  }

  // UserData Navigation (Watchlist, Ratings)
  single<NavEntryProvider>(named<Navigation.UserDataRoute>()) {
    { onNavigate ->
      userDataScreen(onNavigate = onNavigate)
    }
  }

  // Fullscreen Onboarding Navigation
  single<NavEntryProvider>(named<Navigation.Onboarding.FullScreenRoute>()) {
    { onNavigate ->
      fullscreenOnboarding(onNavigate = onNavigate)
    }
  }

  // Modal Onboarding Navigation
  single<NavEntryProvider>(named<Navigation.Onboarding.ModalRoute>()) {
    { onNavigate ->
      modalOnboarding(onNavigate = onNavigate)
    }
  }

  // TMDB Auth Navigation
  single<NavEntryProvider>(named<Navigation.TMDBAuthRoute>()) {
    { onNavigate ->
      tmdbAuthScreen(onNavigate = onNavigate)
    }
  }

  // Profile Navigation
  single<NavEntryProvider>(named<Navigation.ProfileRoute>()) {
    { onNavigate ->
      profileScreen(onNavigate = onNavigate)
    }
  }

  // User Lists Navigation
  single<NavEntryProvider>(named<Navigation.ListsRoute>()) {
    { onNavigate ->
      listsScreen(onNavigate = onNavigate)
    }
  }

  // List Details Navigation
  single<NavEntryProvider>(named<Navigation.ListDetailsRoute>()) {
    { onNavigate ->
      listDetailsScreen(onNavigate = onNavigate)
    }
  }

  // Create List Navigation
  single<NavEntryProvider>(named<Navigation.CreateListRoute>()) {
    { onNavigate ->
      createListScreen(onNavigate = onNavigate)
    }
  }

  // Edit List Navigation
  single<NavEntryProvider>(named<Navigation.EditListRoute>()) {
    { onNavigate ->
      editListScreen(onNavigate = onNavigate)
    }
  }

  // Add To List Navigation
  single<NavEntryProvider>(named<Navigation.AddToListRoute>()) {
    { onNavigate ->
      addToListScreen(onNavigate = onNavigate)
    }
  }

  // WebView Navigation
  single<NavEntryProvider>(named<Navigation.WebViewRoute>()) {
    { onNavigate ->
      webViewScreen(onNavigate = onNavigate)
    }
  }

  // Requests Navigation
  single<NavEntryProvider>(named<Navigation.JellyseerrRequestsRoute>()) {
    { onNavigate ->
      requestsScreen(onNavigate = onNavigate)
    }
  }

  // Action Menu Navigation
  single<NavEntryProvider>(named<Navigation.ActionMenuRoute.Media>()) {
    { onNavigate ->
      defaultMediaActionMenu(onNavigation = onNavigate)
    }
  }

  // Discover Navigation
  single<NavEntryProvider>(named<Navigation.DiscoverRoute>()) {
    { onNavigate ->
      discoverScreen(onNavigate = onNavigate)
    }
  }

  // Poster Navigation
  single<NavEntryProvider>(named<Navigation.MediaPosterRoute>()) {
    { onNavigate ->
      posterScreen(onNavigate = onNavigate)
    }
  }

  single<NavEntryProvider>(named<Navigation.MediaListsRoute>()) {
    { onNavigate ->
      mediaListsScreen(onNavigate = onNavigate)
    }
  }

  single<NavEntryProvider>(named<Navigation.SeasonRoute>()) {
    { onNavigate ->
      seasonScreen(onNavigate = onNavigate)
    }
  }

  single<NavEntryProvider>(named<Navigation.EpisodeRoute>()) {
    { onNavigate ->
      episodeScreen(onNavigate = onNavigate)
    }
  }

  single<NavEntryProvider>(named<Navigation.CollectionRoute>()) {
    { onNavigate ->
      collectionsScreen(onNavigate = onNavigate)
    }
  }

  single<NavEntryProvider>(named<Navigation.UpdaterRoute>()) {
    { onNavigate ->
      updaterScreen(onNavigate = onNavigate)
    }
  }

  single<NavEntryProvider>(named<Navigation.AppUpdatesSettingsRoute>()) {
    { onNavigate ->
      appUpdatesScreen(onNavigate = onNavigate)
    }
  }

  single<List<NavEntryProvider>> {
    getAll<NavEntryProvider>()
  }
}
