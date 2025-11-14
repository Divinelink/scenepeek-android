package com.divinelink.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IosUrlOpener : UrlHandler {
  override fun openUrl(url: String, onError: () -> Unit) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
      UIApplication.sharedApplication.openURL(nsUrl)
    } else {
      onError()
    }
  }
}

@Composable
actual fun rememberUrlHandler(): UrlHandler {
  return remember { IosUrlOpener() }
}

@Composable
actual fun rememberUrlHandlerWithResult(onResult: () -> Unit): UrlHandler {
  return remember { IosUrlOpener() } // TODO Add Result
}
