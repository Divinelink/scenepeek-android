package com.divinelink.feature.lists.user.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.lists.user.ListsScreen

fun EntryProviderScope<Navigation>.listsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.ListsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    LocalNavAnimatedContentScope.current.ListsScreen(
      onNavigate = onNavigate,
    )
  }
}
