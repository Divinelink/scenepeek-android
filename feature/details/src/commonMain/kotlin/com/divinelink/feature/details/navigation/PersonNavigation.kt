package com.divinelink.feature.details.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.details.person.ui.PersonScreen

fun EntryProviderScope<Navigation>.personScreen(onNavigate: (Navigation) -> Unit) {
  entry<PersonRoute>(metadata = TwoPaneScene.twoPane()) { key ->
    PersonScreen(
      route = key,
      onNavigate = onNavigate,
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }
}
