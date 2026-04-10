package com.divinelink.feature.settings.navigation.account

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.LocalScenePeekAppState
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.account.jellyseerr.JellyseerrSettingsScreen

fun EntryProviderScope<Navigation>.jellyseerrSettingsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.JellyseerrSettingsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.JellyseerrSettingsScreen(
      sharedTransitionScope = LocalScenePeekAppState.current.sharedTransitionScope,
      onNavigateUp = { onNavigate(Navigation.Back) },
      withNavigationBar = key.withNavigationBar,
    )
  }
}
