package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun JellyseerrSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  onNavigateUp: () -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: JellyseerrSettingsViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SnackbarMessageHandler(
    snackbarMessage = uiState.snackbarMessage,
    onDismissSnackbar = viewModel::dismissSnackbar,
  )

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(R.string.feature_settings_jellyseerr_account),
    onNavigationClick = onNavigateUp,
  ) { paddingValues ->
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
        is JellyseerrState.Initial -> JellyseerrInitialContent(
          modifier = Modifier
            .padding(paddingValues)
            .testTag(TestTags.Settings.Jellyseerr.INITIAL_BOTTOM_SHEET),
          jellyseerrState = state,
          interaction = viewModel::onJellyseerrInteraction,
        )
        is JellyseerrState.LoggedIn -> JellyseerrLoggedInContent(
          modifier = Modifier
            .padding(paddingValues)
            .testTag(TestTags.Settings.Jellyseerr.LOGGED_IN_BOTTOM_SHEET),
          jellyseerrState = state,
          animatedVisibilityScope = animatedVisibilityScope,
          transitionScope = sharedTransitionScope,
          onLogoutClock = viewModel::onJellyseerrInteraction,
        )
      }
    }
  }
}
