package com.divinelink.feature.webview

import androidx.navigation3.runtime.EntryProviderScope
import com.divinelink.core.navigation.route.Navigation

fun EntryProviderScope<Navigation>.webViewScreen(onNavigate: (Navigation) -> Unit) {
  entry<Navigation.WebViewRoute> { key ->
    WebViewScreen(
      route = key,
      onNavigate = onNavigate,
    )
  }
}
