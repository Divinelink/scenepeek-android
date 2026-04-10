package com.divinelink.feature.settings.navigation.appearance

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.appearance.AppearanceSettingsScreen

fun EntryProviderScope<Navigation>.appearanceSettingsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.AppearanceSettingsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    AppearanceSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigateUp = { onNavigate(Navigation.Back) },
    )
  }
}
