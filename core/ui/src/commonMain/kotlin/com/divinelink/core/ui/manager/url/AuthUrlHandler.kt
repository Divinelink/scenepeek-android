package com.divinelink.core.ui.manager.url

import androidx.compose.runtime.Composable

interface AuthUrlHandler {
  fun openUrl(
    url: String,
    callbackUrlScheme: String,
    onError: () -> Unit = {},
  )
}

@Composable
expect fun rememberAuthUrlHandler(onResult: (Boolean) -> Unit): AuthUrlHandler
