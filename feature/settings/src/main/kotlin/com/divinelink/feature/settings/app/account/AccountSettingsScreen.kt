package com.divinelink.feature.settings.app.account

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.dialog.SimpleAlertDialog
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.login.LoginScreenArgs
import com.divinelink.feature.settings.navigation.SettingsGraph
import com.divinelink.feature.settings.screens.destinations.JellyseerrSettingsScreenDestination
import com.divinelink.feature.settings.screens.destinations.LoginWebViewScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Destination<SettingsGraph>
fun SharedTransitionScope.AccountSettingsScreen(
  navigator: DestinationsNavigator,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: AccountSettingsViewModel = koinViewModel(),
) {
  val viewState = viewModel.viewState.collectAsState()

  LaunchedEffect(viewState.value.navigateToWebView) {
    if (viewState.value.navigateToWebView == true) {
      viewState.value.requestToken?.let { requestToken ->
        val navArgs = LoginScreenArgs(requestToken)
        val destination = LoginWebViewScreenDestination(navArgs)

        navigator.navigate(destination)

        viewModel.onWebViewScreenNavigated()
      }
    }
  }

  SettingsScaffold(
    title = stringResource(id = R.string.preferences__account),
    onNavigationClick = navigator::navigateUp,
  ) { paddingValues ->
    AccountSettingsContent(
      paddingValues = paddingValues,
      onLoginClick = viewModel::login,
      accountDetails = viewState.value.accountDetails,
      jellyseerrAccountDetails = viewState.value.jellyseerrAccountDetails,
      animatedVisibilityScope = animatedVisibilityScope,
      onLogoutClick = viewModel::logoutDialog,
      onNavigateToJellyseerrLogin = { navigator.navigate(JellyseerrSettingsScreenDestination()) },
    )
  }

  viewState.value.alertDialogUiState?.let { uiState ->
    SimpleAlertDialog(
      confirmClick = viewModel::confirmLogout,
      dismissClick = viewModel::dismissLogoutDialog,
      confirmText = UIText.ResourceText(R.string.AccountSettingsScreen__logout),
      dismissText = UIText.ResourceText(uiR.string.core_ui_cancel),
      uiState = uiState,
    )
  }
}
