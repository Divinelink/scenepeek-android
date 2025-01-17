package com.divinelink.feature.settings.login

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.components.SettingsScaffold
import com.divinelink.feature.settings.navigation.SettingsGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination<SettingsGraph>(navArgs = LoginScreenArgs::class)
@SuppressLint("SetJavaScriptEnabled")
fun LoginWebViewScreen(
  navigator: DestinationsNavigator,
  viewModel: LoginWebViewViewModel = koinViewModel(),
) {
  val context = LocalContext.current

  val viewState = viewModel.viewState.collectAsState()

  LaunchedEffect(viewState.value.navigateBack) {
    if (viewState.value.navigateBack) {
      navigator.navigateUp()
    }
  }

  val webView = remember {
    WebView(context).apply {
      settings.javaScriptEnabled = true
      settings.domStorageEnabled = true
      webViewClient = AutoRedirectWebView(
        redirectUrl = viewState.value.redirectUrl,
        onCloseWebView = viewModel::handleSession,
      )
    }
  }

  SettingsScaffold(
    modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
    title = stringResource(R.string.login__title),
    onNavigationClick = navigator::navigateUp,
  ) { paddingValues ->
    AndroidView(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize(),
      factory = { webView },
      update = { it.loadUrl(viewState.value.url) },
    )
  }

  BackHandler(enabled = webView.canGoBack()) {
    webView.goBack()
  }
}
