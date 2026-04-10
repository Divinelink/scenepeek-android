package com.divinelink.feature.settings.navigation.details

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.details.DetailPreferencesSettingsScreen

fun EntryProviderScope<Navigation>.detailsPreferencesSettingsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.DetailsPreferencesSettingsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    DetailPreferencesSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      onNavigateUp = { onNavigate(Navigation.Back) },
    )
  }
}
