package com.divinelink.feature.settings.navigation.links

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.links.LinkHandlingSettingsScreen

fun EntryProviderScope<Navigation>.linkHandlingSettingsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.LinkHandlingSettingsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    LinkHandlingSettingsScreen(
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
      navigateUp = { onNavigate(Navigation.Back) },
    )
  }
}
