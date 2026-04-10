package com.divinelink.feature.settings.navigation.about

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.about.AboutSettingsScreen

fun EntryProviderScope<Navigation>.aboutSettingsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.AboutSettingsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    AboutSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigate = onNavigate,
    )
  }
}
