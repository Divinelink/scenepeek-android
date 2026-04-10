package com.divinelink.feature.media.lists.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.media.lists.ui.MediaListsScreen

fun EntryProviderScope<Navigation>.mediaListsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.MediaListsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.MediaListsScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
