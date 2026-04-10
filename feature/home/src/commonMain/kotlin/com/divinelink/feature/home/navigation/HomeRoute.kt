package com.divinelink.feature.home.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.home.HomeScreen

fun EntryProviderScope<Navigation>.homeScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.HomeRoute>(metadata = TwoPaneScene.twoPane()) {
    HomeScreen(
      onNavigate = onNavigate,
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }
}
