package com.divinelink.core.ui.manager.url

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IosUrlOpener(
  private val onResult: () -> Unit,
) : UrlHandler {
  override fun openUrl(
    url: String,
    onError: () -> Unit,
  ) {
    val nsUrl = NSURL.URLWithString(url) ?: return

    UIApplication.sharedApplication.openURL(
      url = nsUrl,
      options = emptyMap<Any?, Any>(),
      completionHandler = { onResult() },
    )
    /**
     * val rootViewController = UIApplication.sharedApplication
     *       .keyWindow?.rootViewController ?: return
     *
     * val safariVC = SFSafariViewController(uRL = nsUrl)
     * rootViewController.presentViewController(
     *   safariVC,
     *   animated = true,
     *   completion = { onResult() },
     * )
     */
  }
}

@Composable
actual fun rememberUrlHandler(): UrlHandler = remember {
  IosUrlOpener(onResult = { /* Do nothing */ })
}

@Composable
actual fun rememberUrlHandlerWithResult(onResult: () -> Unit): UrlHandler =
  remember { IosUrlOpener(onResult) }
