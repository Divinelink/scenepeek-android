package com.divinelink.feature.profile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.profile.ProfileScreen

fun EntryProviderScope<Navigation>.profileScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.ProfileRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    LocalNavAnimatedContentScope.current.ProfileScreen(
      onNavigate = onNavigate,
    )
  }
}
