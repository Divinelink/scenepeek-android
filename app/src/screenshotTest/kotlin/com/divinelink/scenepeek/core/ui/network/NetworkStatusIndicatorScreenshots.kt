package com.divinelink.scenepeek.core.ui.network

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.android.tools.screenshot.PreviewTest
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.model.network.NetworkState
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.network.NetworkStatusIndicatorPreview
import com.divinelink.core.ui.provider.NetworkStateParameterProvider

@Previews
@PreviewTest
@Composable
fun NetworkStatusIndicatorScreenshots(
  @PreviewParameter(NetworkStateParameterProvider::class) state: NetworkState,
) {
  AppTheme {
    NetworkStatusIndicatorPreview(state)
  }
}
