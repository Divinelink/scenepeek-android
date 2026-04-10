package com.divinelink.feature.onboarding.navigation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.testTag
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.onboarding.ui.IntroModalBottomSheet

@OptIn(ExperimentalComposeUiApi::class)
fun EntryProviderScope<Navigation>.fullscreenOnboarding(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.Onboarding.FullScreenRoute> { _ ->
    BackHandler(enabled = true) {
      // Disable back button
    }

    IntroModalBottomSheet(
      modifier = Modifier.testTag(TestTags.Onboarding.FULLSCREEN),
      onNavigate = onNavigate,
    )
  }
}

@OptIn(ExperimentalComposeUiApi::class)
fun EntryProviderScope<Navigation>.modalOnboarding(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.Onboarding.ModalRoute>(metadata = DialogSceneStrategy.dialog()) { _ ->
    BackHandler(enabled = true) {
      // Disable back button
    }

    IntroModalBottomSheet(
      modifier = Modifier.testTag(TestTags.Onboarding.MODAL),
      onNavigate = onNavigate,
    )
  }
}
