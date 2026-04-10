package com.divinelink.feature.settings.navigation.account

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.LocalScenePeekAppState
import com.divinelink.core.scaffold.TwoPaneScene
import com.divinelink.feature.settings.app.account.AccountSettingsScreen

fun EntryProviderScope<Navigation>.accountSettingsScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.AccountSettingsRoute>(metadata = TwoPaneScene.twoPane()) { _ ->
    AccountSettingsScreen(
      onNavigate = onNavigate,
      sharedTransitionScope = LocalScenePeekAppState.current.sharedTransitionScope, // TODO
      animatedVisibilityScope = LocalNavAnimatedContentScope.current,
    )
  }
}
