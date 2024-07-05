package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.settings.app.account.AccountSettingsViewModel
import com.divinelink.feature.settings.navigation.SettingsGraph
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination<SettingsGraph>
fun JellyseerrLoginScreen(viewModel: AccountSettingsViewModel = hiltViewModel()) {
  val uiState = viewModel.viewState.collectAsState().value

  SnackbarMessageHandler(
    snackbarMessage = uiState.snackbarMessage,
    onDismissSnackbar = viewModel::dismissSnackbar,
  )

  AnimatedContent(
    targetState = uiState.jellyseerrState,
    label = "Jellyseerr State Animated Content",
    contentKey = { state ->
      when (state) {
        is JellyseerrState.Initial -> "Initial"
        is JellyseerrState.LoggedIn -> "LoggedIn"
      }
    },
  ) { state ->
    when (state) {
      is JellyseerrState.Initial -> {
        JellyseerrInitialContent(
          modifier = Modifier.testTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET),
          jellyseerrState = state,
          interaction = viewModel::onJellyseerrInteraction,
        )
      }
      is JellyseerrState.LoggedIn ->
        JellyseerrLoggedInContent(
          modifier = Modifier.testTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET),
          jellyseerrState = state,
          onLogoutClock = {
            viewModel.onJellyseerrInteraction(it)
          },
        )
    }
  }
}