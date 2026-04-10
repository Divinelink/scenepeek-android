package com.divinelink.feature.lists.create.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.feature.lists.create.ui.CreateListScreen

fun EntryProviderScope<Navigation>.createListScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.CreateListRoute> { key ->
    LocalNavAnimatedContentScope.current.CreateListScreen(
      route = key,
      onNavigateUp = { onNavigate(Navigation.Back) },
      onNavigateBackToLists = {},
    )
  }
}

fun EntryProviderScope<Navigation>.editListScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.EditListRoute> { key ->
    LocalNavAnimatedContentScope.current.CreateListScreen(
      route = key,
      onNavigateUp = { onNavigate(Navigation.Back) },
      onNavigateBackToLists = { onNavigate(Navigation.ListsRoute) },
    )
  }
}
