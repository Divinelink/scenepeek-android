package com.divinelink.feature.tmdb.auth.webview

import androidx.compose.runtime.Composable

@Composable
expect fun Webview(
  onCloseWebview: () -> Unit,
  url: String,
)
