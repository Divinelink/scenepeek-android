package com.divinelink.core.ui.manager.url

import androidx.compose.runtime.Composable
import platform.AuthenticationServices.ASWebAuthenticationPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASWebAuthenticationSession
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.darwin.NSObject

@Composable
actual fun rememberAuthUrlHandler(
  onResult: (Boolean) -> Unit,
): AuthUrlHandler = AuthUrlOpener(
  onResult = onResult,
)

private class AuthUrlOpener(
  private val onResult: (Boolean) -> Unit,
) : AuthUrlHandler {
  override fun openUrl(
    url: String,
    callbackUrlScheme: String,
    onError: () -> Unit,
  ) {
    val nsUrl = NSURL.URLWithString(url) ?: return

    ASWebAuthenticationSession(
      uRL = nsUrl,
      callbackURLScheme = callbackUrlScheme,
      completionHandler = { callbackUrl, error ->
        if (error != null) {
          onResult(false)
        } else {
          onResult(true)
        }
      },
    ).apply {
      presentationContextProvider =
        object : NSObject(), ASWebAuthenticationPresentationContextProvidingProtocol {
          override fun presentationAnchorForWebAuthenticationSession(
            session: ASWebAuthenticationSession,
          ) = UIApplication.sharedApplication.keyWindow
        }
      prefersEphemeralWebBrowserSession = false
      start()
    }
  }
}
