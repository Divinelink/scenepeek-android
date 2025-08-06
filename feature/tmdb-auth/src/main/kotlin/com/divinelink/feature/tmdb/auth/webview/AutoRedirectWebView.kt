package com.divinelink.feature.tmdb.auth.webview

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.divinelink.core.commons.ExcludeFromKoverReport

@ExcludeFromKoverReport
class AutoRedirectWebView(
//  private val redirectUrl: String,
  private val onCloseWebView: () -> Unit,
) : WebViewClient() {
  override fun shouldOverrideUrlLoading(
    view: WebView?,
    request: WebResourceRequest?,
  ): Boolean {
    val url = request?.url
    if (url?.scheme == "scenepeek" &&
      url.host == "auth" &&
      url.path == "/redirect"
    ) {
      onCloseWebView()
      CookieManager.getInstance().apply {
        removeAllCookies {
          // Do nothing
        }
        removeSessionCookies {
          // Do nothing
        }
      }
      return true
    }
    return super.shouldOverrideUrlLoading(view, request)
  }
}
