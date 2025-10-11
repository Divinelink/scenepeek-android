package com.divinelink.scenepeek.feature.discover

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.ui.Previews
import com.divinelink.feature.discover.DiscoverUiState
import com.divinelink.feature.discover.ui.DiscoverContentPreview
import com.divinelink.feature.discover.ui.provider.DiscoverUiStateParameterProvider

@Previews
@Composable
fun DiscoverContentScreenshots(
  @PreviewParameter(DiscoverUiStateParameterProvider::class) uiState: DiscoverUiState,
) {
  DiscoverContentPreview(uiState)
}
