package com.andreolas.movierama.settings.app.account

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.R
import com.andreolas.movierama.destinations.LoginWebViewScreenDestination
import com.andreolas.movierama.session.login.ui.LoginScreenArgs
import com.andreolas.movierama.settings.components.SettingsClickItem
import com.andreolas.movierama.settings.components.SettingsScaffold
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun AccountSettingsScreen(
  navigator: DestinationsNavigator,
  viewModel: AccountSettingsViewModel = hiltViewModel()
) {
  val viewState = viewModel.viewState.collectAsState()

  DisposableEffect(viewState.value.navigateToWebView) {
    if (viewState.value.navigateToWebView == true) {
      viewState.value.requestToken?.let { requestToken ->
        val navArgs = LoginScreenArgs(requestToken)
        val destination = LoginWebViewScreenDestination(navArgs)

        navigator.navigate(destination)
      }
    }

    onDispose {
      viewModel.onWebViewScreenNavigated()
    }
  }

  SettingsScaffold(
    title = stringResource(id = R.string.preferences__account),
    onNavigationClick = navigator::navigateUp
  ) { paddingValues ->
    LazyColumn(contentPadding = paddingValues) {
      item {
        SettingsClickItem(
          text = stringResource(id = R.string.AccountSettingsFragment__login),
          onClick = viewModel::login
        )
      }
    }
  }
}
