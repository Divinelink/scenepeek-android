package com.divinelink.feature.details.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.details.media.ui.DetailsScreen

fun EntryProviderScope<Navigation>.detailsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.DetailsRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    DetailsScreen(
      route = key,
      onNavigate = onNavigate,
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }
}
