package com.divinelink.feature.credits.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.credits.ui.CreditsScreen

fun EntryProviderScope<Navigation>.creditsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.CreditsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    LocalNavAnimatedContentScope.current.CreditsScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
