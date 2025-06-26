package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.components.dialog.SimpleAlertDialog
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsScaffold
import org.koin.androidx.compose.koinViewModel
import com.divinelink.core.ui.R as uiR

@Composable
fun AccountSettingsScreen(
  onNavigateUp: () -> Unit,
  onNavigateToJellyseerrSettingsScreen: () -> Unit,
  onNavigateToTMDBAuth: () -> Unit,
  sharedTransitionScope: SharedTransitionScope,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: AccountSettingsViewModel = koinViewModel(),
) {
  val viewState by viewModel.viewState.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.navigateToTMDBAuth.collect {
      onNavigateToTMDBAuth()
    }
  }

  SettingsScaffold(
    animatedVisibilityScope = animatedVisibilityScope,
    title = stringResource(id = R.string.preferences__account),
    onNavigationClick = onNavigateUp,
  ) {
    AccountSettingsContent(
      transitionScope = sharedTransitionScope,
      animatedVisibilityScope = animatedVisibilityScope,
      onLoginClick = viewModel::login,
      uiState = viewState,
      onLogoutClick = viewModel::logoutDialog,
      onNavigateToJellyseerrLogin = onNavigateToJellyseerrSettingsScreen,
    )
  }

  viewState.alertDialogUiState?.let { uiState ->
    SimpleAlertDialog(
      confirmClick = viewModel::confirmLogout,
      dismissClick = viewModel::dismissLogoutDialog,
      confirmText = UIText.ResourceText(R.string.feature_settings_logout),
      dismissText = UIText.ResourceText(uiR.string.core_ui_cancel),
      uiState = uiState,
    )
  }
}
