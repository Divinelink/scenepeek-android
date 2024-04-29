package com.andreolas.movierama.ui.components

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun AutoCloseWebViewScreen(
  navigator: DestinationsNavigator,
  url: String,
  redirectUrl: String
) {
  val context = LocalContext.current
  val currentUrl by remember { mutableStateOf(url) }

  val webView = remember {
    WebView(context).apply {
      settings.javaScriptEnabled = true
      settings.domStorageEnabled = true
      webViewClient = AutoCloseWebView(
        redirectUrl = redirectUrl,
        onCloseWebView = navigator::navigateUp
      )
    }
  }

  AndroidView(
    modifier = Modifier
      .windowInsetsPadding(WindowInsets.statusBars)
      .fillMaxSize(),
    factory = { webView },
    update = { it.loadUrl(currentUrl) }
  )

  BackHandler(enabled = webView.canGoBack()) {
    webView.goBack()
  }
}

class AutoCloseWebView(
  private val redirectUrl: String,
  private val onCloseWebView: () -> Unit
) : WebViewClient() {
  override fun shouldOverrideUrlLoading(
    view: WebView?,
    request: WebResourceRequest?
  ): Boolean {
    val url = request?.url.toString()
    if (url.contains(redirectUrl)) {
      onCloseWebView()
      return true
    }
    return super.shouldOverrideUrlLoading(view, request)
  }
}
