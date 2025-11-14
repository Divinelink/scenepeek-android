package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.dialog.BasicDialog
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.feature_settings_jellyseerr_account
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnimatedVisibilityScope.JellyseerrSettingsScreen(
  sharedTransitionScope: SharedTransitionScope,
  withNavigationBar: Boolean,
  onNavigateUp: () -> Unit,
  viewModel: JellyseerrSettingsViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  SnackbarMessageHandler(
    snackbarMessage = uiState.snackbarMessage,
    onDismissSnackbar = viewModel::dismissSnackbar,
  )

  uiState.dialogState?.let { state ->
    BasicDialog(
      dialogState = state,
      onDismissRequest = {
        viewModel.onJellyseerrInteraction(JellyseerrInteraction.OnDismissDialog)
      },
    )
  }

  SettingsScaffold(
    animatedVisibilityScope = this@JellyseerrSettingsScreen,
    title = stringResource(Res.string.feature_settings_jellyseerr_account),
    onNavigationClick = onNavigateUp,
    withNavigationBar = withNavigationBar,
  ) {
    AnimatedContent(
      targetState = uiState.jellyseerrState,
      label = "Jellyseerr State Animated Content",
      contentKey = { state ->
        when (state) {
          is JellyseerrState.Login -> "Login"
          is JellyseerrState.LoggedIn -> "LoggedIn"
        }
      },
    ) { state ->
      when (state) {
        is JellyseerrState.Login -> JellyseerrLoginContent(
          modifier = Modifier
            .testTag(TestTags.Settings.Jellyseerr.INITIAL_CONTENT),
          state = state,
          interaction = viewModel::onJellyseerrInteraction,
        )
        is JellyseerrState.LoggedIn -> JellyseerrLoggedInContent(
          modifier = Modifier
            .testTag(TestTags.Settings.Jellyseerr.LOGGED_IN_CONTENT),
          jellyseerrState = state,
          animatedVisibilityScope = this@JellyseerrSettingsScreen,
          transitionScope = sharedTransitionScope,
          onLogoutClock = viewModel::onJellyseerrInteraction,
        )
      }
    }
  }
}
