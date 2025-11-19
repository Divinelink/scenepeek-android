package com.divinelink.feature.tmdb.auth.webview

import androidx.compose.runtime.Composable

@Composable
actual fun Webview(onCloseWebview: () -> Unit, url: String) {
  LoginWebViewScreen(
    onCloseWebview = onCloseWebview,
    url = url,
  )
}
