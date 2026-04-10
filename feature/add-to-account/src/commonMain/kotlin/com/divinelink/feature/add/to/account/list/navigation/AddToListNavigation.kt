package com.divinelink.feature.add.to.account.list.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.AddToListRoute
import com.divinelink.feature.add.to.account.list.ui.AddToListScreen

fun EntryProviderScope<Navigation>.addToListScreen(onNavigate: (Navigation) -> Unit) {
  entry<AddToListRoute> { key ->
    LocalNavAnimatedContentScope.current.AddToListScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
