package com.divinelink.feature.webview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.divinelink.core.navigation.route.Navigation

fun NavGraphBuilder.webViewScreen(onNavigate: (Navigation) -> Unit) {
  composable<Navigation.WebViewRoute> {
    val route = it.toRoute<Navigation.WebViewRoute>()
    WebViewScreen(
      route = route,
      onNavigate = onNavigate,
    )
  }
}
