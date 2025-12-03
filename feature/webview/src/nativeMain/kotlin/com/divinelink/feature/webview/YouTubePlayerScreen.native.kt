package com.divinelink.feature.webview

import androidx.compose.ui.Modifier
import com.divinelink.core.navigation.route.Navigation

@androidx.compose.runtime.Composable
actual fun WebViewScreen(
  modifier: Modifier,
  route: Navigation.WebViewRoute,
  onNavigate: (Navigation) -> Unit,
) = Unit
