package com.divinelink.core.ui.manager.url

import androidx.compose.runtime.Composable

interface UrlHandler {
  fun openUrl(
    url: String,
    onError: () -> Unit = {},
  )
}

@Composable
expect fun rememberUrlHandler(): UrlHandler

@Composable
expect fun rememberUrlHandlerWithResult(onResult: () -> Unit): UrlHandler
