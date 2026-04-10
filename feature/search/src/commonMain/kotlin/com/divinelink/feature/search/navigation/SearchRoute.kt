package com.divinelink.feature.search.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.search.ui.SearchScreen

fun EntryProviderScope<Navigation>.searchScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.SearchRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    LocalNavAnimatedContentScope.current.SearchScreen(onNavigate = onNavigate)
  }
}
