package com.divinelink.feature.settings.navigation.settings

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.SettingsScreen

fun EntryProviderScope<Navigation>.settingsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.SettingsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    SettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigate = onNavigate,
    )
  }
}
