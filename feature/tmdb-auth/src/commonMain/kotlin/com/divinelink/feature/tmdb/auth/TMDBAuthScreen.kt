package com.divinelink.feature.tmdb.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.components.Material3CircularProgressIndicator
import com.divinelink.core.ui.rememberUrlHandlerWithResult
import com.divinelink.feature.tmdb.auth.webview.LoginWebViewScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TMDBAuthScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: TMDBAuthViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val urlHandler = rememberUrlHandlerWithResult(
    onResult = { viewModel.handleCloseWeb() },
  )


  LaunchedEffect(Unit) {
    viewModel.onNavigateUp.collect {
      onNavigate(Navigation.Back)
    }
  }


  LaunchedEffect(Unit) {
    viewModel.openUrlTab.collect { url ->
      urlHandler.openUrl(
        url = url,
        onError = { viewModel.setWebViewFallback() },
      )
    }
  }

  Scaffold(
    topBar = {
      if (uiState.webViewFallback) {
        AppTopAppBar(
          text = UIText.ResourceText(Res.string.feature_tmdb_auth_login),
          onNavigateUp = { onNavigate(Navigation.Back) },
        )
      }
    },
  ) { paddingValues ->
    Column {
      Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

      AnimatedContent(uiState.webViewFallback) { useWebView ->
        when (useWebView) {
          true -> LoginWebViewScreen(
            onCloseWebview = { viewModel.createSession() },
            url = uiState.url,
          )
          false -> Box(
            modifier = Modifier
              .testTag(TestTags.LOADING_CONTENT)
              .fillMaxSize(),
          ) {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MaterialTheme.dimensions.keyline_32)
                .wrapContentSize(Alignment.Center),
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Material3CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(),
              )

              Text(
                text = stringResource(Res.string.feature_tmdb_auth_loading_text),
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentSize(Alignment.Center),
                style = MaterialTheme.typography.titleSmall,
              )
            }
          }
        }
      }
    }
  }
}
