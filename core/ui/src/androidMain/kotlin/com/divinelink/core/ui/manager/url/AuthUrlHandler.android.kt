package com.divinelink.core.ui.manager.url

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


@Composable
actual fun rememberAuthUrlHandler(onResult: (Boolean) -> Unit): AuthUrlHandler {
  val context = LocalContext.current

  val launcher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult(),
  ) {
    onResult(false)
  }

  return remember(context) {
    AndroidAuthUrlHandlerWithResult(
      context = context,
      launcher = launcher,
    )
  }
}

private class AndroidAuthUrlHandlerWithResult(
  private val context: Context,
  private val launcher: ActivityResultLauncher<Intent>,
) : AuthUrlHandler {
  override fun openUrl(url: String, callbackUrlScheme: String, onError: () -> Unit) {
    launchCustomTab(
      context = context,
      launcher = launcher,
      url = url,
      webViewFallback = onError,
    )
  }
}
