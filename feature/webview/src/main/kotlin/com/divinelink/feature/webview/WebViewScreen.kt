package com.divinelink.feature.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.AppTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(
  modifier: Modifier = Modifier,
  route: Navigation.WebViewRoute,
  onNavigate: (Navigation) -> Unit,
) {
  val context = LocalContext.current
  var isLoading by remember { mutableStateOf(false) }

  val webView = remember {
    WebView(context).apply {
      settings.javaScriptEnabled = true
      settings.domStorageEnabled = true
      webViewClient = object : WebViewClient() {
        override fun onPageFinished(
          view: WebView?,
          url: String?,
        ) {
          super.onPageFinished(view, url)
          isLoading = false
        }
      }
    }
  }

  Scaffold(
    topBar = {
      AppTopAppBar(
        text = UIText.StringText(route.title),
        onNavigateUp = { onNavigate(Navigation.Back) },
      )
    },
  ) { paddingValues ->
    Column {
      Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding()))

      PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
          isLoading = true
          webView.reload()
        },
        modifier = Modifier
          .fillMaxSize()
          .testTag(TestTags.Components.PULL_TO_REFRESH),
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize(),
        ) {
          AndroidView(
            modifier = modifier
              .weight(1f)
              .verticalScroll(rememberScrollState()),
            factory = { webView },
            update = { it.loadUrl(route.url) },
          )
        }
      }
    }
  }

  BackHandler(enabled = webView.canGoBack()) {
    webView.goBack()
  }
}
