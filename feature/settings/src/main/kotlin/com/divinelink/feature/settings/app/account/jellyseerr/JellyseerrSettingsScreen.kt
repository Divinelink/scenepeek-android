package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.navigation.SettingsGraph
import com.divinelink.feature.settings.navigation.SlideTransition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination<SettingsGraph>(style = SlideTransition::class)
fun JellyseerrSettingsScreen(
  navigator: DestinationsNavigator,
  viewModel: JellyseerrSettingsViewModel = hiltViewModel(),
) {
  val uiState = viewModel.uiState.collectAsState().value

  SnackbarMessageHandler(
    snackbarMessage = uiState.snackbarMessage,
    onDismissSnackbar = viewModel::dismissSnackbar,
  )

  SettingsScaffold(
    title = stringResource(R.string.feature_settings_jellyseerr_account),
    onNavigationClick = navigator::navigateUp,
  ) { paddingValues ->
    AnimatedContent(
      targetState = uiState.jellyseerrState,
      label = "Jellyseerr State Animated Content",
      contentKey = { state ->
        when (state) {
          is JellyseerrState.Loading -> "Loading"
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
          onLogoutClock = {
            viewModel.onJellyseerrInteraction(it)
          },
        )
        JellyseerrState.Loading -> LoadingContent()
      }
    }
  }
}
