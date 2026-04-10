package com.divinelink.feature.settings.navigation.updates

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.updates.AppUpdatesScreen

fun EntryProviderScope<Navigation>.appUpdatesScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.AppUpdatesSettingsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    AppUpdatesScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigate = onNavigate,
    )
  }
}
