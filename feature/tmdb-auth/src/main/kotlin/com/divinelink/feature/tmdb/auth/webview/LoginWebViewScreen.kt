package com.divinelink.feature.tmdb.auth.webview

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
@SuppressLint("SetJavaScriptEnabled")
fun LoginWebViewScreen(
  modifier: Modifier = Modifier,
  onCloseWebview: () -> Unit,
  url: String,
) {
  val context = LocalContext.current

  val webView = remember {
    WebView(context).apply {
      settings.javaScriptEnabled = true
      settings.domStorageEnabled = true
      webViewClient = AutoRedirectWebView(
        onCloseWebView = onCloseWebview,
      )
    }
  }

  AndroidView(
    modifier = modifier
      .fillMaxSize(),
    factory = { webView },
    update = { it.loadUrl(url) },
  )

  BackHandler(enabled = webView.canGoBack()) {
    webView.goBack()
  }
}
