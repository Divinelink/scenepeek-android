package com.andreolas.movierama.settings.app.account

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.R
import com.andreolas.movierama.destinations.LoginWebViewScreenDestination
import com.andreolas.movierama.session.login.ui.LoginScreenArgs
import com.andreolas.movierama.settings.components.SettingsClickItem
import com.andreolas.movierama.settings.components.SettingsScaffold
import com.andreolas.movierama.settings.components.SettingsTextItem
import com.andreolas.movierama.ui.TestTags
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.dialog.SimpleAlertDialog
import com.andreolas.movierama.ui.theme.dimensions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun AccountSettingsScreen(
  navigator: DestinationsNavigator,
  viewModel: AccountSettingsViewModel = hiltViewModel()
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
    LazyColumn(contentPadding = paddingValues) {
      item {
        AnimatedContent(
          targetState = viewState.value.accountDetails,
          label = "Account details animation",
        ) { accountDetails ->
          if (accountDetails != null) {
            Column {
              SettingsTextItem(
                title = stringResource(
                  id = R.string.AccountSettingsScreen__logged_in_as, accountDetails.username
                ),
                summary = null
              )
              Button(
                onClick = viewModel::logoutDialog,
                modifier = Modifier
                  .testTag(TestTags.Settings.Account.LOGOUT_BUTTON)
                  .padding(start = MaterialTheme.dimensions.keyline_16)
              ) {
                Text(text = stringResource(id = R.string.AccountSettingsScreen__logout))
              }
            }
          } else {
            SettingsClickItem(
              modifier = Modifier.testTag(TestTags.Settings.Account.LOGIN_BUTTON),
              text = stringResource(id = R.string.AccountSettingsScreen__login),
              onClick = viewModel::login
            )
          }
        }
      }
    }

    viewState.value.alertDialogUiState?.let { uiState ->
      SimpleAlertDialog(
        confirmClick = viewModel::confirmLogout,
        dismissClick = viewModel::dismissLogoutDialog,
        confirmText = UIText.ResourceText(R.string.AccountSettingsScreen__logout),
        dismissText = UIText.ResourceText(R.string.cancel),
        uiState = uiState
      )
    }
  }
}
