package com.divinelink.feature.collections.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.collections.ui.CollectionsScreen

fun EntryProviderScope<Navigation>.collectionsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.CollectionRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.CollectionsScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
