package com.divinelink.feature.onboarding.navigation

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.onboarding.ui.IntroModalBottomSheet

@OptIn(ExperimentalComposeUiApi::class)
fun NavGraphBuilder.fullscreenOnboarding(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.Onboarding.FullScreenRoute> {
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
fun NavGraphBuilder.modalOnboarding(onNavigate: (Navigation) -> Unit) {
  dialog<Navigation.Onboarding.ModalRoute> {
    BackHandler(enabled = true) {
      // Disable back button
    }

    IntroModalBottomSheet(
      modifier = Modifier.testTag(TestTags.Onboarding.MODAL),
      onNavigate = onNavigate,
    )
  }
}
