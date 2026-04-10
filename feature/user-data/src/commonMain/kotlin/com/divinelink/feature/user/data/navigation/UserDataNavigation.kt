package com.divinelink.feature.user.data.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.UserDataRoute
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.user.data.UserDataScreen

fun EntryProviderScope<Navigation>.userDataScreen(onNavigate: (Navigation) -> Unit) {
  entry<UserDataRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.UserDataScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
