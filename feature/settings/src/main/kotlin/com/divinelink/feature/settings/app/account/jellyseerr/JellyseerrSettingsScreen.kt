package com.divinelink.feature.settings.app.account.jellyseerr

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.model.jellyseerr.JellyseerrState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.navigation.SettingsGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Destination<SettingsGraph>
fun SharedTransitionScope.JellyseerrSettingsScreen(
  navigator: DestinationsNavigator,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: JellyseerrSettingsViewModel = koinViewModel(),
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
          onLogoutClock = viewModel::onJellyseerrInteraction,
        )
      }
    }
  }
}
