package com.divinelink.feature.webview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.navigation.route.Navigation

@Composable
expect fun WebViewScreen(
  modifier: Modifier = Modifier,
  route: Navigation.WebViewRoute,
  onNavigate: (Navigation) -> Unit,
)
