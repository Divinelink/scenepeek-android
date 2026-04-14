package com.divinelink.scenepeek.di

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.testTag
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.model.media.decodeToMediaItem
import com.divinelink.core.navigation.Navigator
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.AccountSettingsRoute
import com.divinelink.core.navigation.route.Navigation.AppearanceSettingsRoute
import com.divinelink.core.navigation.route.Navigation.JellyseerrSettingsRoute
import com.divinelink.core.scaffold.LocalScenePeekAppState
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.add.to.account.list.ui.AddToListScreen
import com.divinelink.feature.add.to.account.modal.ActionMenuEntryPoint
import com.divinelink.feature.add.to.account.modal.ActionMenuModal
import com.divinelink.feature.collections.ui.CollectionsScreen
import com.divinelink.feature.credits.ui.CreditsScreen
import com.divinelink.feature.details.media.ui.DetailsScreen
import com.divinelink.feature.details.person.ui.PersonScreen
import com.divinelink.feature.details.poster.PosterScreen
import com.divinelink.feature.discover.ui.DiscoverScreen
import com.divinelink.feature.episode.ui.EpisodeScreen
import com.divinelink.feature.home.HomeScreen
import com.divinelink.feature.lists.create.ui.CreateListScreen
import com.divinelink.feature.lists.details.ui.ListDetailsScreen
import com.divinelink.feature.lists.user.ListsScreen
import com.divinelink.feature.media.lists.ui.MediaListsScreen
import com.divinelink.feature.onboarding.ui.IntroModalBottomSheet
import com.divinelink.feature.profile.ProfileScreen
import com.divinelink.feature.requests.ui.RequestsScreen
import com.divinelink.feature.search.ui.SearchScreen
import com.divinelink.feature.season.ui.SeasonScreen
import com.divinelink.feature.settings.app.SettingsScreen
import com.divinelink.feature.settings.app.about.AboutSettingsScreen
import com.divinelink.feature.settings.app.account.AccountSettingsScreen
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsScreen
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsScreen
import com.divinelink.feature.settings.app.details.DetailPreferencesSettingsScreen
import com.divinelink.feature.settings.app.links.LinkHandlingSettingsScreen
import com.divinelink.feature.settings.app.updates.AppUpdatesScreen
import com.divinelink.feature.tmdb.auth.TMDBAuthScreen
import com.divinelink.feature.updater.ui.UpdaterScreen
import com.divinelink.feature.user.data.UserDataScreen
import com.divinelink.feature.webview.WebViewScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class, ExperimentalComposeUiApi::class)
val navigationModule = module {

  single { Navigator() }

  navigation<Navigation.HomeRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    HomeScreen(
      onNavigate = navigator::navigate,
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }

  navigation<Navigation.PersonRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    PersonScreen(
      route = key,
      onNavigate = navigator::navigate,
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }

  navigation<Navigation.DetailsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    DetailsScreen(
      route = key,
      onNavigate = navigator::navigate,
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }

  navigation<Navigation.SearchRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.SearchScreen(onNavigate = navigator::navigate)
  }

  navigation<Navigation.SettingsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    SettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigate = navigator::navigate,
    )
  }

  navigation<AccountSettingsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    AccountSettingsScreen(
      onNavigate = navigator::navigate,
      sharedTransitionScope = LocalScenePeekAppState.current.sharedTransitionScope, // TODO
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }

  navigation<JellyseerrSettingsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.JellyseerrSettingsScreen(
      sharedTransitionScope = LocalScenePeekAppState.current.sharedTransitionScope,
      onNavigateUp = { navigator.goBack() },
      withNavigationBar = key.withNavigationBar,
    )
  }

  navigation<AppearanceSettingsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    AppearanceSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigateUp = { navigator.goBack() },
    )
  }

  navigation<Navigation.DetailsPreferencesSettingsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    DetailPreferencesSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigateUp = { navigator.goBack() },
    )
  }

  navigation<Navigation.LinkHandlingSettingsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    LinkHandlingSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      navigateUp = { navigator.goBack() },
    )
  }

  navigation<Navigation.AboutSettingsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    AboutSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.CreditsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.CreditsScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.UserDataRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.UserDataScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.Onboarding.FullScreenRoute> {
    val navigator = get<Navigator>()
    BackHandler(enabled = true) {
      // Disable back button
    }

    IntroModalBottomSheet(
      modifier = Modifier.testTag(TestTags.Onboarding.FULLSCREEN),
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.Onboarding.ModalRoute>(metadata = DialogSceneStrategy.dialog()) {
    val navigator = get<Navigator>()
    BackHandler(enabled = true) {
      // Disable back button
    }

    IntroModalBottomSheet(
      modifier = Modifier.testTag(TestTags.Onboarding.MODAL),
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.TMDBAuthRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    TMDBAuthScreen(onNavigate = navigator::navigate)
  }

  navigation<Navigation.ProfileRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.ProfileScreen(
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.ListsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.ListsScreen(
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.ListDetailsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.ListDetailsScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.CreateListRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.CreateListScreen(
      route = key,
      onNavigateUp = navigator::goBack,
      onNavigateBackToLists = {},
    )
  }

  navigation<Navigation.EditListRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.CreateListScreen(
      route = key,
      onNavigateUp = navigator::goBack,
      onNavigateBackToLists = { navigator.navigate(Navigation.ListsRoute) },
    )
  }

  navigation<Navigation.AddToListRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.AddToListScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.WebViewRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    WebViewScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.JellyseerrRequestsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.RequestsScreen(
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.ActionMenuRoute.Media>(DialogSceneStrategy.dialog()) { key ->
    val navigator = get<Navigator>()
    val mediaItem = key.encodedMediaItem.decodeToMediaItem()

    ActionMenuModal(
      mediaItem = mediaItem,
      entryPoint = ActionMenuEntryPoint.Other,
      onDismissRequest = navigator::goBack,
      onMultiSelect = {},
      onNavigateToAddToList = {
        navigator.navigate(
          Navigation.AddToListRoute(
            id = mediaItem.id,
            mediaType = mediaItem.mediaType.value,
          ),
        )
      },
    )
  }

  navigation<Navigation.DiscoverRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.DiscoverScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.MediaPosterRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.PosterScreen(
      path = key.posterPath,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.MediaListsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.MediaListsScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.SeasonRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.SeasonScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.EpisodeRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.EpisodeScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.CollectionRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    val navigator = get<Navigator>()
    LocalNavAnimatedContentScope.current.CollectionsScreen(
      route = key,
      onNavigate = navigator::navigate,
    )
  }

  navigation<Navigation.UpdaterRoute>(metadata = DialogSceneStrategy.dialog()) {
    val navigator = get<Navigator>()
    UpdaterScreen(
      onNavigate = navigator::navigate,
    )

  }

  navigation<Navigation.AppUpdatesSettingsRoute>(metadata = TwoPaneScene.twoPane()) {
    val navigator = get<Navigator>()
    AppUpdatesScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigate = navigator::navigate,
    )
  }
}
