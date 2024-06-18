import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class AutoRedirectWebView(
  private val redirectUrl: String,
  private val onCloseWebView: (String) -> Unit
) : WebViewClient() {
  override fun shouldOverrideUrlLoading(
    view: WebView?,
    request: WebResourceRequest?
  ): Boolean {
    val url = request?.url.toString()
    if (url.contains(redirectUrl)) {
      onCloseWebView(url)
      return true
    }
    return super.shouldOverrideUrlLoading(view, request)
  }
}
